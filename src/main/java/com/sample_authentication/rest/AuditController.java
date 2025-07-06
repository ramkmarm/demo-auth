package com.sample_authentication.rest;


import com.sample_authentication.model.ApiAuditLog;
import com.sample_authentication.repository.ApiAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

    private final ApiAuditLogRepository auditLogRepository;

    @GetMapping("/logs")
    public List<ApiAuditLog> getAllLogs() {
        return auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }
}
