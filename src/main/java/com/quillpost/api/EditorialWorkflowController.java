package com.quillpost.api;

import com.quillpost.content.domain.Post;
import com.quillpost.content.dto.PostSummaryDto;
import com.quillpost.editorial.EditorialWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/posts/{postId}/workflow")
public class EditorialWorkflowController {

    private final EditorialWorkflowService workflowService;

    public EditorialWorkflowController(EditorialWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping("/submit")
    public ResponseEntity<PostSummaryDto> submit(@PathVariable UUID workspaceId, @PathVariable UUID postId) {
        return ResponseEntity.ok(toSummary(workflowService.submitForReview(workspaceId, postId)));
    }

    @PostMapping("/approve")
    public ResponseEntity<PostSummaryDto> approve(
        @PathVariable UUID workspaceId,
        @PathVariable UUID postId,
        @RequestBody ApproveRequest request) {
        return ResponseEntity.ok(toSummary(workflowService.approve(workspaceId, postId, request.publishAt())));
    }

    @PostMapping("/request-changes")
    public ResponseEntity<PostSummaryDto> requestChanges(@PathVariable UUID workspaceId, @PathVariable UUID postId) {
        return ResponseEntity.ok(toSummary(workflowService.requestChanges(workspaceId, postId)));
    }

    @PostMapping("/archive")
    public ResponseEntity<PostSummaryDto> archive(@PathVariable UUID workspaceId, @PathVariable UUID postId) {
        return ResponseEntity.ok(toSummary(workflowService.archive(workspaceId, postId)));
    }

    private PostSummaryDto toSummary(Post post) {
        return new PostSummaryDto(
            post.getId(), post.getTitle(), post.getSlug(), post.getStatus(),
            post.getPublishAt(), post.getReadingTimeMinutes(), post.getUpdatedAt());
    }

    public record ApproveRequest(Instant publishAt) {
    }
}
