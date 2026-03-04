package com.quillpost.feeds;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeedController {

    private final FeedService feedService;
    private final FeedCacheService feedCacheService;

    public FeedController(FeedService feedService, FeedCacheService feedCacheService) {
        this.feedService = feedService;
        this.feedCacheService = feedCacheService;
    }

    @GetMapping(value = "/w/{workspaceSlug}/feed.rss", produces = "application/rss+xml")
    public ResponseEntity<String> rss(@PathVariable String workspaceSlug) {
        return ResponseEntity.ok().contentType(feedService.rssMediaType()).body(feedCacheService.rss(workspaceSlug));
    }

    @GetMapping(value = "/w/{workspaceSlug}/feed.atom", produces = "application/atom+xml")
    public ResponseEntity<String> atom(@PathVariable String workspaceSlug) {
        return ResponseEntity.ok().contentType(feedService.atomMediaType()).body(feedCacheService.atom(workspaceSlug));
    }

    @GetMapping(value = "/w/{workspaceSlug}/feed.json", produces = "application/feed+json")
    public ResponseEntity<String> jsonFeed(@PathVariable String workspaceSlug) {
        return ResponseEntity.ok().contentType(feedService.jsonFeedMediaType()).body(feedCacheService.jsonFeed(workspaceSlug));
    }

    @GetMapping(value = "/w/{workspaceSlug}/sitemap.xml", produces = "application/xml")
    public ResponseEntity<String> sitemap(@PathVariable String workspaceSlug) {
        return ResponseEntity.ok().body(feedService.sitemap(workspaceSlug));
    }

    @GetMapping(value = "/robots.txt", produces = "text/plain")
    public String robots() {
        return "User-agent: *\nAllow: /\n";
    }
}
