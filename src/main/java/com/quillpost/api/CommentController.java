package com.quillpost.api;

import com.quillpost.comments.CommentService;
import com.quillpost.content.domain.Comment;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> submit(@PathVariable UUID postId, @Valid @RequestBody SubmitCommentRequest request) {
        return ResponseEntity.ok(commentService.submit(
            postId, request.authorEmail(), request.authorName(), request.bodyMarkdown(), request.parentId()));
    }

    @GetMapping("/workspaces/{workspaceId}/comments/moderation")
    public List<Comment> moderationQueue(@PathVariable UUID workspaceId) {
        return commentService.moderationQueue(workspaceId);
    }

    @PostMapping("/comments/{commentId}/approve")
    public ResponseEntity<Comment> approve(@PathVariable UUID commentId) {
        return ResponseEntity.ok(commentService.approve(commentId));
    }

    public record SubmitCommentRequest(
        @NotBlank @Email String authorEmail,
        @NotBlank String authorName,
        @NotBlank String bodyMarkdown,
        UUID parentId
    ) {
    }
}
