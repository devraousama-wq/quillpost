package com.quillpost.api;

import com.quillpost.auth.CurrentUserService;
import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.dto.PostDetailDto;
import com.quillpost.content.dto.PostSummaryDto;
import com.quillpost.content.service.PostAuthoringService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/posts")
public class PostAuthoringController {

    private final PostAuthoringService postAuthoringService;
    private final CurrentUserService currentUserService;

    public PostAuthoringController(PostAuthoringService postAuthoringService, CurrentUserService currentUserService) {
        this.postAuthoringService = postAuthoringService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<PostSummaryDto> listDrafts(@PathVariable UUID workspaceId) {
        return postAuthoringService.listDrafts(workspaceId);
    }

    @PostMapping
    public ResponseEntity<PostSummaryDto> create(
        @PathVariable UUID workspaceId,
        Authentication authentication,
        @Valid @RequestBody SavePostRequest request) {
        UserAccount user = currentUserService.requireUser(authentication);
        Post post = postAuthoringService.createDraft(workspaceId, user, request.title(), request.bodyMarkdown());
        return ResponseEntity.ok(new PostSummaryDto(
            post.getId(), post.getTitle(), post.getSlug(), post.getStatus(),
            post.getPublishAt(), post.getReadingTimeMinutes(), post.getUpdatedAt()));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostSummaryDto> autosave(
        @PathVariable UUID workspaceId,
        @PathVariable UUID postId,
        @Valid @RequestBody SavePostRequest request) {
        Post post = postAuthoringService.autosave(workspaceId, postId, request.title(), request.bodyMarkdown());
        return ResponseEntity.ok(new PostSummaryDto(
            post.getId(), post.getTitle(), post.getSlug(), post.getStatus(),
            post.getPublishAt(), post.getReadingTimeMinutes(), post.getUpdatedAt()));
    }

    @GetMapping("/{postId}/preview")
    public PostDetailDto preview(@PathVariable UUID workspaceId, @PathVariable UUID postId) {
        return postAuthoringService.preview(workspaceId, postId);
    }

    public record SavePostRequest(
        @NotBlank @Size(max = 300) String title,
        @NotBlank String bodyMarkdown
    ) {
    }
}
