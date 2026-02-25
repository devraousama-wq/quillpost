package com.quillpost.content.service;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostRevision;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.dto.PostRevisionDto;
import com.quillpost.content.repository.PostRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PostRollbackService {

    private final PostRepository posts;
    private final PostRevisionService postRevisionService;

    public PostRollbackService(PostRepository posts, PostRevisionService postRevisionService) {
        this.posts = posts;
        this.postRevisionService = postRevisionService;
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canManage(#workspaceId, authentication)")
    public Post rollback(UUID workspaceId, UUID postId, UUID revisionId, UserAccount actor) {
        Post post = posts.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));
        if (!post.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("workspace mismatch");
        }
        PostRevision revision = postRevisionService.list(workspaceId, postId).stream()
            .filter(r -> r.getId().equals(revisionId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("revision not found"));
        postRevisionService.snapshot(workspaceId, post, actor);
        post.setBodyMarkdown(revision.getBodyMarkdown());
        post.touchUpdatedAt();
        return posts.save(post);
    }

    public List<PostRevisionDto> listDtos(UUID workspaceId, UUID postId) {
        return postRevisionService.list(workspaceId, postId).stream()
            .map(r -> new PostRevisionDto(
                r.getId(), r.getPost().getId(), r.getAuthor().getId(),
                r.getTitle(), r.getBodyMarkdown(), r.getCreatedAt()))
            .toList();
    }
}
