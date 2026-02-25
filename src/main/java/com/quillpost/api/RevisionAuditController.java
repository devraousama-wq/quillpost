package com.quillpost.api;

import com.quillpost.auth.CurrentUserService;
import com.quillpost.audit.domain.AuditLogEntry;
import com.quillpost.audit.AuditLogService;
import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.dto.PostRevisionDto;
import com.quillpost.content.service.PostRevisionService;
import com.quillpost.content.service.PostRollbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}")
public class RevisionAuditController {

    private final PostRevisionService postRevisionService;
    private final PostRollbackService postRollbackService;
    private final AuditLogService auditLogService;
    private final CurrentUserService currentUserService;

    public RevisionAuditController(
        PostRevisionService postRevisionService,
        PostRollbackService postRollbackService,
        AuditLogService auditLogService,
        CurrentUserService currentUserService) {
        this.postRevisionService = postRevisionService;
        this.postRollbackService = postRollbackService;
        this.auditLogService = auditLogService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/posts/{postId}/revisions")
    public List<PostRevisionDto> revisions(@PathVariable UUID workspaceId, @PathVariable UUID postId) {
        return postRollbackService.listDtos(workspaceId, postId);
    }

    @GetMapping("/audit-log")
    public List<AuditLogEntry> auditLog(@PathVariable UUID workspaceId) {
        return auditLogService.listForWorkspace(workspaceId);
    }

    @PostMapping("/posts/{postId}/revisions/{revisionId}/rollback")
    public ResponseEntity<Void> rollback(
        @PathVariable UUID workspaceId,
        @PathVariable UUID postId,
        @PathVariable UUID revisionId,
        Authentication authentication) {
        UserAccount user = currentUserService.requireUser(authentication);
        postRollbackService.rollback(workspaceId, postId, revisionId, user);
        return ResponseEntity.noContent().build();
    }
}
