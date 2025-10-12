package com.honeypot.credentials.repository;

import com.honeypot.credentials.model.CredentialLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialLogRepository extends JpaRepository<CredentialLog, Long> {
}
