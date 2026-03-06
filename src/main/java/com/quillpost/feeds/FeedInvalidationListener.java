package com.quillpost.feeds;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class FeedInvalidationListener {

    private final FeedCacheService feedCacheService;

    public FeedInvalidationListener(FeedCacheService feedCacheService) {
        this.feedCacheService = feedCacheService;
    }

    @TransactionalEventListener
    public void onPostPublished(PostPublishedEvent event) {
        feedCacheService.invalidate(event.workspaceSlug());
    }

    public record PostPublishedEvent(String workspaceSlug, PostStatus status) {
        public static PostPublishedEvent of(Post post) {
            return new PostPublishedEvent(post.getWorkspace().getSlug(), post.getStatus());
        }
    }
}
