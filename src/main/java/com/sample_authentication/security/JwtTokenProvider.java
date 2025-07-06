package com.sample_authentication.security;

import com.sample_authentication.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, String roleName) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", new SimpleGrantedAuthority(roleName));
        claims.put("userName", username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = null;
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            userDetails = this.userDetailsService.loadUserByUsername(getUsername(claims));
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    public String getUsername(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Transactional
    public boolean verifyToken(String token, HttpServletRequest httpServletRequest){
        String requestUrl = httpServletRequest.getRequestURI();
        Boolean isAllowed;
        try {
            User webUser =  userDetailsService.getUser(getUsername(token));
            isAllowed = true;

            isAllowed = isURIAllowedForUser(webUser, requestUrl);
            if (!isAllowed) {
                throw new RuntimeException("Request URI is not accessible for this user role.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return true;
    }

    private boolean isURIAllowedForUser(User webUser, String requestUrl) {
        boolean isAdminApi = Boolean.FALSE;
        if(requestUrl != null && requestUrl.contains("/wo")) {
            isAdminApi = Boolean.TRUE;
        }
        if(isAdminApi) {
            if(webUser.getRole().equalsIgnoreCase("ADMIN") ||
                    webUser.getRole().equalsIgnoreCase("MANAGER")) return Boolean.TRUE;
            else return Boolean.FALSE;
        } return Boolean.TRUE;

    }

}

