package com.quillpost.api;

import com.quillpost.audit.domain.AuditLogEntry;
import com.quillpost.audit.AuditLogService;
import com.quillpost.content.domain.PostRevision;
import com.quillpost.content.service.PostRevisionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}")
public class RevisionAuditController {

    private final PostRevisionService postRevisionService;
    private final AuditLogService auditLogService;

    public RevisionAuditController(PostRevisionService postRevisionService, AuditLogService auditLogService) {
        this.postRevisionService = postRevisionService;
        this.auditLogService = auditLogService;
    }

    @GetMapping("/posts/{postId}/revisions")
    public List<PostRevision> revisions(@PathVariable UUID workspaceId, @PathVariable UUID postId) {
        return postRevisionService.list(workspaceId, postId);
    }

    @GetMapping("/audit-log")
    public List<AuditLogEntry> auditLog(@PathVariable UUID workspaceId) {
        return auditLogService.listForWorkspace(workspaceId);
    }
}
