package com.quillpost.api;

import com.quillpost.auth.CurrentUserService;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.notifications.Notification;
import com.quillpost.notifications.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final CurrentUserService currentUserService;

    public NotificationController(NotificationService notificationService, CurrentUserService currentUserService) {
        this.notificationService = notificationService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<Notification> inbox(Authentication authentication) {
        UserAccount user = currentUserService.requireUser(authentication);
        return notificationService.inbox(user.getId());
    }
}
