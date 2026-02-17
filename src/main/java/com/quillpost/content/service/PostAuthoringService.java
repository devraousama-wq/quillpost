package com.quillpost.content.service;

import com.quillpost.content.domain.Excerpt;
import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.domain.ReadingTime;
import com.quillpost.content.domain.Slug;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.dto.PostDetailDto;
import com.quillpost.content.dto.PostSummaryDto;
import com.quillpost.content.markdown.MarkdownService;
import com.quillpost.content.repository.PostRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PostAuthoringService {

    private final PostRepository posts;
    private final WorkspaceService workspaceService;
    private final MarkdownService markdownService;

    public PostAuthoringService(PostRepository posts, WorkspaceService workspaceService, MarkdownService markdownService) {
        this.posts = posts;
        this.workspaceService = workspaceService;
        this.markdownService = markdownService;
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canAuthor(#workspaceId, authentication)")
    public Post createDraft(UUID workspaceId, UserAccount author, String title, String bodyMarkdown) {
        Workspace workspace = workspaceService.requireWorkspace(workspaceId);
        String slug = resolveUniqueSlug(workspace.getId(), Slug.fromTitle(title).value());
        Post post = new Post(workspace, author, title, slug);
        applyBody(post, bodyMarkdown);
        return posts.save(post);
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canAuthor(#workspaceId, authentication)")
    public Post autosave(UUID workspaceId, UUID postId, String title, String bodyMarkdown) {
        Post post = posts.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));
        if (!post.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("workspace mismatch");
        }
        if (post.getStatus() != PostStatus.DRAFT) {
            throw new IllegalStateException("only drafts can be autosaved");
        }
        post.setBodyMarkdown(bodyMarkdown);
        if (title != null && !title.isBlank()) {
            applyBody(post, bodyMarkdown);
        } else {
            applyBody(post, bodyMarkdown);
        }
        post.touchUpdatedAt();
        return posts.save(post);
    }

    @PreAuthorize("@workspaceAccess.canAuthor(#workspaceId, authentication)")
    public PostDetailDto preview(UUID workspaceId, UUID postId) {
        Post post = posts.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));
        if (!post.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("workspace mismatch");
        }
        return toDetail(post, markdownService.toHtml(post.getBodyMarkdown()));
    }

    public List<PostSummaryDto> listDrafts(UUID workspaceId) {
        return posts.findByWorkspaceIdAndStatusOrderByUpdatedAtDesc(workspaceId, PostStatus.DRAFT).stream()
            .map(this::toSummary)
            .toList();
    }

    private void applyBody(Post post, String bodyMarkdown) {
        post.setBodyMarkdown(bodyMarkdown);
        post.setExcerpt(Excerpt.of(bodyMarkdown).value());
        post.setReadingTimeMinutes(ReadingTime.fromWordCount(markdownService.countWords(bodyMarkdown)).minutes());
    }

    private String resolveUniqueSlug(UUID workspaceId, String base) {
        String candidate = base;
        int suffix = 2;
        while (posts.findByWorkspaceIdAndSlug(workspaceId, candidate).isPresent()) {
            candidate = base + "-" + suffix++;
        }
        return candidate;
    }

    private PostSummaryDto toSummary(Post post) {
        return new PostSummaryDto(
            post.getId(), post.getTitle(), post.getSlug(), post.getStatus(),
            post.getPublishAt(), post.getReadingTimeMinutes(), post.getUpdatedAt());
    }

    private PostDetailDto toDetail(Post post, String html) {
        return new PostDetailDto(
            post.getId(), post.getTitle(), post.getSlug(), post.getStatus(),
            post.getBodyMarkdown(), html, post.getExcerpt(), post.getReadingTimeMinutes(),
            post.getPublishAt(), post.getUpdatedAt());
    }
}
