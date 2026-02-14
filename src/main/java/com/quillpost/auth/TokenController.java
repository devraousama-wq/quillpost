package com.quillpost.auth;

import com.quillpost.auth.jwt.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class TokenController {

    private final JwtTokenService jwtTokenService;

    public TokenController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> token(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String token = jwtTokenService.issueToken(authentication);
        return ResponseEntity.ok(Map.of("access_token", token, "token_type", "Bearer"));
    }
}
