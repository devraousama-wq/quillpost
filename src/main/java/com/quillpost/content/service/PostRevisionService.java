package com.quillpost.content.service;

import com.quillpost.audit.AuditLogService;
import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostRevision;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.repository.PostRevisionRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PostRevisionService {

    private final PostRevisionRepository revisions;
    private final AuditLogService auditLogService;

    public PostRevisionService(PostRevisionRepository revisions, AuditLogService auditLogService) {
        this.revisions = revisions;
        this.auditLogService = auditLogService;
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canAuthor(#workspaceId, authentication)")
    public PostRevision snapshot(UUID workspaceId, Post post, UserAccount author) {
        if (!post.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("workspace mismatch");
        }
        PostRevision revision = revisions.save(new PostRevision(
            post, author, post.getTitle(), post.getBodyMarkdown()));
        auditLogService.record(
            workspaceId,
            author.getId(),
            "post.revision.created",
            "Post",
            post.getId(),
            "{\"title\":\"" + escape(post.getTitle()) + "\"}");
        return revision;
    }

    @PreAuthorize("@workspaceAccess.canAuthor(#workspaceId, authentication)")
    public List<PostRevision> list(UUID workspaceId, UUID postId) {
        return revisions.findByPostIdOrderByCreatedAtDesc(postId);
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\"", "\\\"");
    }
}
