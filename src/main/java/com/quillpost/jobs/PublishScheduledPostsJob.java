package com.quillpost.jobs;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class PublishScheduledPostsJob {

    private static final Logger log = LoggerFactory.getLogger(PublishScheduledPostsJob.class);

    private final PostRepository posts;
    private final JobLockService jobLockService;

    public PublishScheduledPostsJob(PostRepository posts, JobLockService jobLockService) {
        this.posts = posts;
        this.jobLockService = jobLockService;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void run() {
        if (!jobLockService.tryAcquire("publish-scheduled-posts", 55)) {
            return;
        }
        Instant now = Instant.now();
        List<Post> due = posts.findByStatusAndPublishAtLessThanEqual(PostStatus.SCHEDULED, now);
        for (Post post : due) {
            post.setStatus(PostStatus.PUBLISHED);
            post.touchUpdatedAt();
            posts.save(post);
            log.info("published scheduled post {}", post.getId());
        }
    }
}
