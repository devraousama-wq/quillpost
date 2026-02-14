package com.quillpost.content.service;

import com.quillpost.content.domain.RoleType;
import com.quillpost.content.repository.WorkspaceMembershipRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Component("workspaceAccess")
public class WorkspaceAccessEvaluator {

    private static final Set<RoleType> MANAGERS = EnumSet.of(RoleType.OWNER, RoleType.EDITOR);

    private final WorkspaceMembershipRepository memberships;

    public WorkspaceAccessEvaluator(WorkspaceMembershipRepository memberships) {
        this.memberships = memberships;
    }

    public boolean canManage(UUID workspaceId, Authentication authentication) {
        return hasRole(workspaceId, authentication, MANAGERS);
    }

    public boolean canAuthor(UUID workspaceId, Authentication authentication) {
        return hasRole(workspaceId, authentication, EnumSet.of(
            RoleType.OWNER, RoleType.EDITOR, RoleType.AUTHOR, RoleType.CONTRIBUTOR));
    }

    private boolean hasRole(UUID workspaceId, Authentication authentication, Set<RoleType> allowed) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return memberships.findAll().stream()
            .filter(m -> m.getWorkspace().getId().equals(workspaceId))
            .filter(m -> m.getUser().getEmail().equalsIgnoreCase(authentication.getName()))
            .anyMatch(m -> allowed.contains(m.getRole()));
    }
}
