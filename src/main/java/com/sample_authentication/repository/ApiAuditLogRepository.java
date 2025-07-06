package com.sample_authentication.repository;

import com.sample_authentication.model.ApiAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiAuditLogRepository extends JpaRepository<ApiAuditLog, Long> {}
