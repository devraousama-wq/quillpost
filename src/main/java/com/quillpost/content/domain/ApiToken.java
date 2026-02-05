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
@Table(name = "api_tokens")
public class ApiToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(nullable = false, length = 120)
    private String label;

    @Column(nullable = false, length = 64, unique = true)
    private String tokenHash;

    @Column(nullable = false, length = 120)
    private String scopes;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column
    private Instant lastUsedAt;

    protected ApiToken() {
    }

    public ApiToken(Workspace workspace, UserAccount user, String label, String tokenHash, String scopes) {
        this.workspace = workspace;
        this.user = user;
        this.label = label;
        this.tokenHash = tokenHash;
        this.scopes = scopes;
    }

    public UUID getId() {
        return id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public UserAccount getUser() {
        return user;
    }

    public String getLabel() {
        return label;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public String getScopes() {
        return scopes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastUsedAt() {
        return lastUsedAt;
    }

    public void markUsed() {
        this.lastUsedAt = Instant.now();
    }
}
