package com.quillpost.jobs;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.repository.PostRepository;
import com.quillpost.content.repository.UserAccountRepository;
import com.quillpost.notifications.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeeklyDigestJob {

    private static final Logger log = LoggerFactory.getLogger(WeeklyDigestJob.class);

    private final PostRepository posts;
    private final UserAccountRepository users;
    private final NotificationService notifications;
    private final JobLockService jobLockService;

    public WeeklyDigestJob(
        PostRepository posts,
        UserAccountRepository users,
        NotificationService notifications,
        JobLockService jobLockService) {
        this.posts = posts;
        this.users = users;
        this.notifications = notifications;
        this.jobLockService = jobLockService;
    }

    @Scheduled(cron = "0 0 9 * * MON")
    public void sendDigest() {
        if (!jobLockService.tryAcquire("weekly-digest", 600)) {
            return;
        }
        List<Post> published = posts.findAll().stream()
            .filter(p -> p.getStatus() == PostStatus.PUBLISHED)
            .toList();
        for (UserAccount user : users.findAll()) {
            if (!user.isDigestEnabled()) {
                continue;
            }
            notifications.notifyUser(
                user.getId(),
                user.getEmail(),
                "Weekly digest",
                published.size() + " posts published this week on your workspaces");
        }
        log.info("weekly digest sent to opted-in users");
    }
}
