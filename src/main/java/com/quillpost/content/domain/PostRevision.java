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
@Table(name = "post_revisions")
public class PostRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private UserAccount author;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(columnDefinition = "text")
    private String bodyMarkdown;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected PostRevision() {
    }

    public PostRevision(Post post, UserAccount author, String title, String bodyMarkdown) {
        this.post = post;
        this.author = author;
        this.title = title;
        this.bodyMarkdown = bodyMarkdown;
    }

    public UUID getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public UserAccount getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getBodyMarkdown() {
        return bodyMarkdown;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
