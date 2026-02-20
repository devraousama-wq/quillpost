package com.quillpost.audit;

import com.quillpost.audit.domain.AuditLogEntry;
import com.quillpost.audit.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public AuditLogEntry record(UUID workspaceId, UUID userId, String action, String entityType, UUID entityId, String diffJson) {
        return auditLogRepository.save(new AuditLogEntry(workspaceId, userId, action, entityType, entityId, diffJson));
    }

    public List<AuditLogEntry> listForWorkspace(UUID workspaceId) {
        return auditLogRepository.findByWorkspaceIdOrderByCreatedAtDesc(workspaceId);
    }
}
