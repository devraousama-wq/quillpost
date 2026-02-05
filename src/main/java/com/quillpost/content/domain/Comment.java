package com.quillpost.content.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment {

    public enum CommentStatus {
        PENDING,
        APPROVED,
        SPAM,
        BLOCKED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(nullable = false, length = 320)
    private String authorEmail;

    @Column(nullable = false, length = 120)
    private String authorName;

    @Column(nullable = false, columnDefinition = "text")
    private String bodyMarkdown;

    @Column(nullable = false, length = 16)
    private String status = CommentStatus.PENDING.name();

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected Comment() {
    }

    public Comment(Post post, String authorEmail, String authorName, String bodyMarkdown) {
        this.post = post;
        this.authorEmail = authorEmail;
        this.authorName = authorName;
        this.bodyMarkdown = bodyMarkdown;
    }

    public UUID getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getBodyMarkdown() {
        return bodyMarkdown;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
