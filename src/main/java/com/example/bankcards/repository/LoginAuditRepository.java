package com.example.bankcards.repository;

import com.example.bankcards.entity.LoginAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAuditRepository
        extends JpaRepository<LoginAudit, Long> {
}