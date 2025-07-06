package com.sample_authentication.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample_authentication.model.ApiAuditLog;
import com.sample_authentication.repository.ApiAuditLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
public class AuditLoggingAspect {

    @Autowired
    private ApiAuditLogRepository auditLogRepository;

    @Lazy
    @Autowired
    private ObjectMapper objectMapper;

    @Around("execution(* com.sample_authentication.rest..*(..))")
    public Object logApiCalls(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String endpoint = request.getRequestURI();

        if(endpoint.contains("/auth")) {
            return joinPoint.proceed();
        }
        String method = request.getMethod();
        String username = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .orElse("Anonymous");

        String requestBody = getRequestBody(joinPoint.getArgs());
        Object result = joinPoint.proceed();
        String responseBody = objectMapper.writeValueAsString(result);

        ApiAuditLog log = new ApiAuditLog();
        log.setEndpoint(endpoint);
        log.setHttpMethod(method);
        log.setUsername(username);
        log.setRequestPayload(requestBody);
        log.setResponsePayload(responseBody);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
        return result;
    }

    private String getRequestBody(Object[] args) {
        try {
            return Arrays.stream(args)
                    .filter(arg -> !(arg instanceof HttpServletRequest ||
                            arg instanceof HttpServletResponse ||
                            arg instanceof BindingResult))
                    .map(arg -> {
                        try {
                            return objectMapper.writeValueAsString(arg);
                        } catch (JsonProcessingException e) {
                            return "Unserializable";
                        }
                    })
                    .collect(Collectors.joining(", "));
        } catch (Exception e) {
            return "Error reading request";
        }
    }
}
