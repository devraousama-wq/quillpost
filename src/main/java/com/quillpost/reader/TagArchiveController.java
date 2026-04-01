package com.quillpost.reader;

import com.quillpost.content.domain.Post;
import com.quillpost.content.repository.WorkspaceRepository;
import com.quillpost.content.service.PostTagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Controller
public class TagArchiveController {

    private final WorkspaceRepository workspaces;
    private final PostTagService postTagService;

    public TagArchiveController(WorkspaceRepository workspaces, PostTagService postTagService) {
        this.workspaces = workspaces;
        this.postTagService = postTagService;
    }

    @GetMapping("/w/{workspaceSlug}/tags/{tagSlug}")
    public String tagArchive(@PathVariable String workspaceSlug, @PathVariable String tagSlug, Model model) {
        UUID workspaceId = workspaces.findBySlug(workspaceSlug).orElseThrow().getId();
        List<Post> posts = postTagService.postsForTag(workspaceId, tagSlug);
        model.addAttribute("workspaceSlug", workspaceSlug);
        model.addAttribute("tagSlug", tagSlug);
        model.addAttribute("posts", posts);
        return "reader/tag-archive";
    }
}
