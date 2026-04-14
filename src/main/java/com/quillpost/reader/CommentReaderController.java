package com.quillpost.reader;

import com.quillpost.comments.CommentService;
import com.quillpost.content.domain.Comment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Controller
public class CommentReaderController {

    private final CommentService commentService;

    public CommentReaderController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/w/{workspaceSlug}/p/{postSlug}/comments")
    public String postComments(@PathVariable String workspaceSlug, @PathVariable UUID postId, Model model) {
        List<Comment> comments = commentService.approvedForPost(postId);
        model.addAttribute("comments", comments);
        model.addAttribute("workspaceSlug", workspaceSlug);
        return "reader/comments";
    }
}
