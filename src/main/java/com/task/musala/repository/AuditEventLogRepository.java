package com.task.musala.repository;

import com.task.musala.entity.AuditEventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditEventLogRepository extends JpaRepository<AuditEventLog, Long> {
}

