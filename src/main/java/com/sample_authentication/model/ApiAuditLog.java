package com.sample_authentication.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_audit_logs")
@Data
public class ApiAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String endpoint;
    private String requestPayload;
    private String responsePayload;
    private String httpMethod;
    private String username;
    private LocalDateTime timestamp;
}
