package com.quillpost.api;

import com.quillpost.auth.CurrentUserService;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.dto.WorkspaceDto;
import com.quillpost.content.service.WorkspaceService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final CurrentUserService currentUserService;

    public WorkspaceController(WorkspaceService workspaceService, CurrentUserService currentUserService) {
        this.workspaceService = workspaceService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<WorkspaceDto> list(Authentication authentication) {
        UserAccount user = currentUserService.requireUser(authentication);
        return workspaceService.listForUser(user.getId());
    }

    @PostMapping
    public ResponseEntity<WorkspaceDto> create(Authentication authentication, @Valid @RequestBody CreateWorkspaceRequest request) {
        UserAccount user = currentUserService.requireUser(authentication);
        Workspace workspace = workspaceService.createWorkspace(user, request.slug(), request.name());
        return ResponseEntity.ok(new WorkspaceDto(workspace.getId(), workspace.getSlug(), workspace.getName()));
    }

    public record CreateWorkspaceRequest(
        @NotBlank @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$") String slug,
        @NotBlank @Size(max = 200) String name
    ) {
    }
}
