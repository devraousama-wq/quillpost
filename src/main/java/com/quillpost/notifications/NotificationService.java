package com.quillpost.notifications;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notifications;
    private final JavaMailSender mailSender;

    public NotificationService(NotificationRepository notifications, JavaMailSender mailSender) {
        this.notifications = notifications;
        this.mailSender = mailSender;
    }

    @Transactional
    public Notification notifyUser(UUID userId, String email, String title, String body) {
        Notification notification = notifications.save(new Notification(userId, title, body));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(title);
        message.setText(body);
        mailSender.send(message);
        return notification;
    }

    public List<Notification> inbox(UUID userId) {
        return notifications.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @EventListener
    public void onPostSubmitted(PostSubmittedEvent event) {
        notifyUser(event.userId(), event.email(), "Post submitted", "Your draft was submitted for review.");
    }

    public record PostSubmittedEvent(UUID userId, String email) {
    }
}
