package com.quillpost.content.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "workspace_settings")
public class WorkspaceSettings {

    @Id
    private UUID workspaceId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @Column(nullable = false, length = 120)
    private String themeName = "default";

    @Column(nullable = false, length = 32)
    private String primaryColor = "#2d6a4f";

    @Column(nullable = false, length = 32)
    private String backgroundColor = "#faf9f7";

    @Column(nullable = false)
    private boolean commentsEnabled = true;

    @Column(nullable = false)
    private boolean digestEnabled = true;

    protected WorkspaceSettings() {
    }

    public WorkspaceSettings(Workspace workspace) {
        this.workspace = workspace;
        this.workspaceId = workspace.getId();
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isCommentsEnabled() {
        return commentsEnabled;
    }

    public void setCommentsEnabled(boolean commentsEnabled) {
        this.commentsEnabled = commentsEnabled;
    }

    public boolean isDigestEnabled() {
        return digestEnabled;
    }

    public void setDigestEnabled(boolean digestEnabled) {
        this.digestEnabled = digestEnabled;
    }
}
