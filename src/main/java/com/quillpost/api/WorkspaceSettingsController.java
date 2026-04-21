package com.quillpost.api;

import com.quillpost.content.domain.WorkspaceSettings;
import com.quillpost.content.service.WorkspaceSettingsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/settings")
public class WorkspaceSettingsController {

    private final WorkspaceSettingsService workspaceSettingsService;

    public WorkspaceSettingsController(WorkspaceSettingsService workspaceSettingsService) {
        this.workspaceSettingsService = workspaceSettingsService;
    }

    @GetMapping
    public WorkspaceSettings get(@PathVariable UUID workspaceId) {
        return workspaceSettingsService.getOrCreate(workspaceId);
    }

    @PutMapping
    public WorkspaceSettings update(@PathVariable UUID workspaceId, @RequestBody WorkspaceSettingsService.UpdateSettingsRequest request) {
        return workspaceSettingsService.update(workspaceId, request);
    }
}
