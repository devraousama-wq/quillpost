package com.quillpost.admin;

import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.dto.PostSummaryDto;
import com.quillpost.content.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PostRepository posts;

    public AdminController(PostRepository posts) {
        this.posts = posts;
    }

    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/w/{workspaceId}/posts")
    public String posts(
        @PathVariable UUID workspaceId,
        @RequestParam(defaultValue = "DRAFT") PostStatus status,
        Model model) {
        model.addAttribute("posts", posts.findByWorkspaceIdAndStatusOrderByUpdatedAtDesc(workspaceId, status).stream()
            .map(p -> new PostSummaryDto(p.getId(), p.getTitle(), p.getSlug(), p.getStatus(), p.getPublishAt(), p.getReadingTimeMinutes(), p.getUpdatedAt()))
            .toList());
        model.addAttribute("workspaceId", workspaceId);
        model.addAttribute("status", status);
        return "admin/posts";
    }

    @GetMapping("/w/{workspaceId}/editor")
    public String editor(@PathVariable UUID workspaceId, Model model) {
        model.addAttribute("workspaceId", workspaceId);
        return "admin/editor";
    }
}
