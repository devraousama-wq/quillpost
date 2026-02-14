package com.quillpost.auth;

import com.quillpost.content.domain.RoleType;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.domain.WorkspaceMembership;
import com.quillpost.content.repository.UserAccountRepository;
import com.quillpost.content.repository.WorkspaceMembershipRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentUserService {

    private final UserAccountRepository users;

    public CurrentUserService(UserAccountRepository users) {
        this.users = users;
    }

    public UserAccount requireUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("authentication required");
        }
        return users.findByEmailIgnoreCase(authentication.getName())
            .orElseThrow(() -> new IllegalStateException("user not found"));
    }
}
