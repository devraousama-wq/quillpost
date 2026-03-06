package com.quillpost.feeds;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FeedCacheService {

    private final FeedService feedService;
    private final Map<String, String> rssCache = new ConcurrentHashMap<>();
    private final Map<String, String> atomCache = new ConcurrentHashMap<>();
    private final Map<String, String> jsonCache = new ConcurrentHashMap<>();

    public FeedCacheService(FeedService feedService) {
        this.feedService = feedService;
    }

    public String rss(String workspaceSlug) {
        return rssCache.computeIfAbsent(workspaceSlug, feedService::rss);
    }

    public String atom(String workspaceSlug) {
        return atomCache.computeIfAbsent(workspaceSlug, feedService::atom);
    }

    public String jsonFeed(String workspaceSlug) {
        return jsonCache.computeIfAbsent(workspaceSlug, feedService::jsonFeed);
    }

    public void invalidate(String workspaceSlug) {
        rssCache.remove(workspaceSlug);
        atomCache.remove(workspaceSlug);
        jsonCache.remove(workspaceSlug);
    }
}
