package com.quillpost.editorial;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.repository.PostRepository;
import com.quillpost.feeds.FeedInvalidationListener.PostPublishedEvent;
import com.quillpost.notifications.NotificationService.PostSubmittedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class EditorialWorkflowService {

    private static final Map<PostStatus, Set<PostStatus>> TRANSITIONS = new EnumMap<>(PostStatus.class);

    static {
        TRANSITIONS.put(PostStatus.DRAFT, Set.of(PostStatus.IN_REVIEW));
        TRANSITIONS.put(PostStatus.IN_REVIEW, Set.of(PostStatus.DRAFT, PostStatus.SCHEDULED, PostStatus.PUBLISHED));
        TRANSITIONS.put(PostStatus.SCHEDULED, Set.of(PostStatus.PUBLISHED, PostStatus.DRAFT));
        TRANSITIONS.put(PostStatus.PUBLISHED, Set.of(PostStatus.ARCHIVED));
        TRANSITIONS.put(PostStatus.ARCHIVED, Set.of());
    }

    private final PostRepository posts;
    private final ApplicationEventPublisher events;

    public EditorialWorkflowService(PostRepository posts, ApplicationEventPublisher events) {
        this.posts = posts;
        this.events = events;
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canAuthor(#workspaceId, authentication)")
    public Post submitForReview(UUID workspaceId, UUID postId) {
        return transition(workspaceId, postId, PostStatus.IN_REVIEW);
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canManage(#workspaceId, authentication)")
    public Post approve(UUID workspaceId, UUID postId, Instant publishAt) {
        Post post = transition(workspaceId, postId, publishAt == null ? PostStatus.PUBLISHED : PostStatus.SCHEDULED);
        if (publishAt != null) {
            post.setPublishAt(publishAt);
        } else {
            post.setPublishAt(Instant.now());
        }
        return posts.save(post);
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canManage(#workspaceId, authentication)")
    public Post requestChanges(UUID workspaceId, UUID postId) {
        return transition(workspaceId, postId, PostStatus.DRAFT);
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canManage(#workspaceId, authentication)")
    public Post archive(UUID workspaceId, UUID postId) {
        return transition(workspaceId, postId, PostStatus.ARCHIVED);
    }

    private Post transition(UUID workspaceId, UUID postId, PostStatus target) {
        Post post = posts.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));
        if (!post.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("workspace mismatch");
        }
        PostStatus current = post.getStatus();
        Set<PostStatus> allowed = TRANSITIONS.getOrDefault(current, Set.of());
        if (!allowed.contains(target)) {
            throw new IllegalStateException("invalid transition " + current + " -> " + target);
        }
        post.setStatus(target);
        post.touchUpdatedAt();
        Post saved = posts.save(post);
        if (target == PostStatus.IN_REVIEW) {
            events.publishEvent(new PostSubmittedEvent(post.getAuthor().getId(), post.getAuthor().getEmail()));
        }
        if (target == PostStatus.PUBLISHED) {
            events.publishEvent(PostPublishedEvent.of(saved));
        }
        return saved;
    }
}
