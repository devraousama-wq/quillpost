package com.quillpost.audit.repository;

import com.quillpost.audit.domain.AuditLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLogEntry, UUID> {

    List<AuditLogEntry> findByWorkspaceIdOrderByCreatedAtDesc(UUID workspaceId);
}
