package com.quillpost.api;

import com.quillpost.content.domain.ApiToken;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.repository.ApiTokenRepository;
import com.quillpost.content.service.WorkspaceService;
import com.quillpost.auth.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/tokens")
public class ApiTokenController {

    private final ApiTokenRepository apiTokens;
    private final WorkspaceService workspaceService;
    private final CurrentUserService currentUserService;

    public ApiTokenController(ApiTokenRepository apiTokens, WorkspaceService workspaceService, CurrentUserService currentUserService) {
        this.apiTokens = apiTokens;
        this.workspaceService = workspaceService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<CreateTokenResponse> create(
        @PathVariable UUID workspaceId,
        Authentication authentication,
        @RequestBody CreateTokenRequest request) throws NoSuchAlgorithmException {
        UserAccount user = currentUserService.requireUser(authentication);
        Workspace workspace = workspaceService.requireWorkspace(workspaceId);
        String raw = UUID.randomUUID().toString().replace("-", "");
        String hash = sha256(raw);
        ApiToken token = apiTokens.save(new ApiToken(workspace, user, request.label(), hash, request.scopes()));
        return ResponseEntity.ok(new CreateTokenResponse(token.getId(), raw));
    }

    private String sha256(String value) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }

    public record CreateTokenRequest(String label, String scopes) {
    }

    public record CreateTokenResponse(UUID id, String token) {
    }
}
