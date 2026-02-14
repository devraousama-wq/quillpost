package com.quillpost.content.service;

import com.quillpost.content.domain.RoleType;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.domain.WorkspaceMembership;
import com.quillpost.content.dto.WorkspaceDto;
import com.quillpost.content.repository.WorkspaceMembershipRepository;
import com.quillpost.content.repository.WorkspaceRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaces;
    private final WorkspaceMembershipRepository memberships;

    public WorkspaceService(WorkspaceRepository workspaces, WorkspaceMembershipRepository memberships) {
        this.workspaces = workspaces;
        this.memberships = memberships;
    }

    @Transactional
    public Workspace createWorkspace(UserAccount owner, String slug, String name) {
        workspaces.findBySlug(slug).ifPresent(w -> {
            throw new IllegalArgumentException("slug taken");
        });
        Workspace workspace = workspaces.save(new Workspace(slug, name));
        memberships.save(new WorkspaceMembership(workspace, owner, RoleType.OWNER));
        return workspace;
    }

    public List<WorkspaceDto> listForUser(UUID userId) {
        return memberships.findByUserId(userId).stream()
            .map(m -> new WorkspaceDto(
                m.getWorkspace().getId(),
                m.getWorkspace().getSlug(),
                m.getWorkspace().getName()))
            .toList();
    }

    @PreAuthorize("@workspaceAccess.canManage(#workspaceId, authentication)")
    public Workspace requireWorkspace(UUID workspaceId) {
        return workspaces.findById(workspaceId)
            .orElseThrow(() -> new IllegalArgumentException("workspace not found"));
    }

    public RoleType roleFor(UUID workspaceId, UUID userId) {
        return memberships.findByWorkspaceIdAndUserId(workspaceId, userId)
            .map(WorkspaceMembership::getRole)
            .orElseThrow(() -> new IllegalStateException("not a workspace member"));
    }
}
