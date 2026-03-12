package com.quillpost.reader;

import com.quillpost.feeds.FeedInvalidationListener.PostPublishedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ReaderCacheEvictionListener {

    private final ReaderSiteService readerSiteService;

    public ReaderCacheEvictionListener(ReaderSiteService readerSiteService) {
        this.readerSiteService = readerSiteService;
    }

    @TransactionalEventListener
    public void onPostPublished(PostPublishedEvent event) {
        readerSiteService.evictHomeCache(event.workspaceSlug());
    }
}
