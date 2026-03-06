package com.quillpost.reader;

import com.quillpost.content.domain.Post;
import com.quillpost.content.dto.PostDetailDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ReaderController {

    private final ReaderSiteService readerSiteService;

    public ReaderController(ReaderSiteService readerSiteService) {
        this.readerSiteService = readerSiteService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("siteName", "Quillpost");
        return "reader/home";
    }

    @GetMapping("/w/{workspaceSlug}")
    public String workspaceHome(@PathVariable String workspaceSlug, Model model) {
        List<Post> posts = readerSiteService.recentPublished(workspaceSlug);
        model.addAttribute("workspaceSlug", workspaceSlug);
        model.addAttribute("posts", posts);
        return "reader/workspace";
    }

    @GetMapping("/w/{workspaceSlug}/p/{postSlug}")
    public String postDetail(@PathVariable String workspaceSlug, @PathVariable String postSlug, Model model) {
        PostDetailDto post = readerSiteService.publishedPost(workspaceSlug, postSlug);
        model.addAttribute("post", post);
        model.addAttribute("workspaceSlug", workspaceSlug);
        return "reader/post";
    }
}
