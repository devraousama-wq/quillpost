package com.quillpost.auth;

import com.quillpost.auth.dto.AuthUserDto;
import com.quillpost.auth.dto.RegisterRequest;
import com.quillpost.content.domain.UserAccount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LocalAuthService localAuthService;

    public AuthController(LocalAuthService localAuthService) {
        this.localAuthService = localAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthUserDto> register(@Valid @RequestBody RegisterRequest request) {
        UserAccount account = localAuthService.register(
            request.email(), request.displayName(), request.password());
        return ResponseEntity.ok(new AuthUserDto(account.getId(), account.getEmail(), account.getDisplayName()));
    }
}
