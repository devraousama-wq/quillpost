package com.quillpost.content.service;

import com.quillpost.content.domain.Workspace;
import com.quillpost.content.domain.WorkspaceSettings;
import com.quillpost.content.repository.WorkspaceSettingsRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WorkspaceSettingsService {

    private final WorkspaceSettingsRepository settings;
    private final WorkspaceService workspaceService;

    public WorkspaceSettingsService(WorkspaceSettingsRepository settings, WorkspaceService workspaceService) {
        this.settings = settings;
        this.workspaceService = workspaceService;
    }

    @Transactional
    public WorkspaceSettings getOrCreate(UUID workspaceId) {
        return settings.findById(workspaceId).orElseGet(() -> {
            Workspace workspace = workspaceService.requireWorkspace(workspaceId);
            return settings.save(new WorkspaceSettings(workspace));
        });
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canManage(#workspaceId, authentication)")
    public WorkspaceSettings update(UUID workspaceId, UpdateSettingsRequest request) {
        WorkspaceSettings current = getOrCreate(workspaceId);
        if (request.themeName() != null) current.setThemeName(request.themeName());
        if (request.primaryColor() != null) current.setPrimaryColor(request.primaryColor());
        if (request.backgroundColor() != null) current.setBackgroundColor(request.backgroundColor());
        if (request.commentsEnabled() != null) current.setCommentsEnabled(request.commentsEnabled());
        if (request.digestEnabled() != null) current.setDigestEnabled(request.digestEnabled());
        return settings.save(current);
    }

    public record UpdateSettingsRequest(
        String themeName,
        String primaryColor,
        String backgroundColor,
        Boolean commentsEnabled,
        Boolean digestEnabled
    ) {
    }
}
