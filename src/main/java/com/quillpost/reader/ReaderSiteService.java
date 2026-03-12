package com.quillpost.reader;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.dto.PostDetailDto;
import com.quillpost.content.markdown.MarkdownService;
import com.quillpost.content.repository.PostRepository;
import com.quillpost.content.repository.WorkspaceRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReaderSiteService {

    private final WorkspaceRepository workspaces;
    private final PostRepository posts;
    private final MarkdownService markdownService;

    public ReaderSiteService(WorkspaceRepository workspaces, PostRepository posts, MarkdownService markdownService) {
        this.workspaces = workspaces;
        this.posts = posts;
        this.markdownService = markdownService;
    }

    @Cacheable(cacheNames = "readerHome", key = "#workspaceSlug")
    public List<Post> recentPublished(String workspaceSlug) {
        UUID workspaceId = workspaces.findBySlug(workspaceSlug)
            .orElseThrow(() -> new IllegalArgumentException("workspace not found"))
            .getId();
        return posts.findByWorkspaceIdAndStatusOrderByUpdatedAtDesc(workspaceId, PostStatus.PUBLISHED);
    }

    public PostDetailDto publishedPost(String workspaceSlug, String postSlug) {
        UUID workspaceId = workspaces.findBySlug(workspaceSlug)
            .orElseThrow(() -> new IllegalArgumentException("workspace not found"))
            .getId();
        Post post = posts.findByWorkspaceIdAndSlug(workspaceId, postSlug)
            .orElseThrow(() -> new IllegalArgumentException("post not found"));
        if (post.getStatus() != PostStatus.PUBLISHED) {
            throw new IllegalStateException("post not published");
        }
        return new PostDetailDto(
            post.getId(), post.getTitle(), post.getSlug(), post.getStatus(),
            post.getBodyMarkdown(), markdownService.toHtml(post.getBodyMarkdown()),
            post.getExcerpt(), post.getReadingTimeMinutes(), post.getPublishAt(), post.getUpdatedAt());
    }

    @CacheEvict(cacheNames = "readerHome", key = "#workspaceSlug")
    public void evictHomeCache(String workspaceSlug) {
    }
}
