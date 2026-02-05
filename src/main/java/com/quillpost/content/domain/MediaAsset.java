package com.quillpost.content.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "media_assets")
public class MediaAsset {

    public enum MediaType {
        IMAGE,
        FILE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Column(nullable = false, length = 260)
    private String filename;

    @Column(nullable = false, length = 120)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private MediaType mediaType;

    @Column(nullable = false)
    private long byteSize;

    @Column(length = 300)
    private String altText;

    @Column(nullable = false)
    private Instant uploadedAt = Instant.now();

    protected MediaAsset() {
    }

    public MediaAsset(Workspace workspace, String filename, String contentType, MediaType mediaType, long byteSize) {
        this.workspace = workspace;
        this.filename = filename;
        this.contentType = contentType;
        this.mediaType = mediaType;
        this.byteSize = byteSize;
    }

    public UUID getId() {
        return id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public String getFilename() {
        return filename;
    }

    public String getContentType() {
        return contentType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public long getByteSize() {
        return byteSize;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }
}
