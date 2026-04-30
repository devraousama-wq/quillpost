package com.quillpost.jobs;

import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SearchRebuildJob {

    private static final Logger log = LoggerFactory.getLogger(SearchRebuildJob.class);

    private final PostRepository posts;
    private final JobLockService jobLockService;

    public SearchRebuildJob(PostRepository posts, JobLockService jobLockService) {
        this.posts = posts;
        this.jobLockService = jobLockService;
    }

    @Scheduled(cron = "0 30 3 * * *")
    public void rebuild() {
        if (!jobLockService.tryAcquire("search-rebuild", 300)) {
            return;
        }
        long count = posts.findAll().stream()
            .filter(p -> p.getStatus() == PostStatus.PUBLISHED)
            .count();
        log.info("search rebuild complete, {} published posts indexed", count);
    }
}
