package com.quillpost.api;

import com.quillpost.auth.CurrentUserService;
import com.quillpost.content.domain.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountPreferencesController {

    private final CurrentUserService currentUserService;
    private final com.quillpost.content.repository.UserAccountRepository users;

    public AccountPreferencesController(CurrentUserService currentUserService, com.quillpost.content.repository.UserAccountRepository users) {
        this.currentUserService = currentUserService;
        this.users = users;
    }

    @PostMapping("/digest/opt-out")
    public ResponseEntity<Void> optOut(Authentication authentication) {
        UserAccount user = currentUserService.requireUser(authentication);
        user.setDigestEnabled(false);
        users.save(user);
        return ResponseEntity.noContent().build();
    }
}
