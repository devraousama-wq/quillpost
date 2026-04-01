package com.quillpost.api;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.Tag;
import com.quillpost.content.repository.PostRepository;
import com.quillpost.content.service.PostTagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/posts/{postId}/tags")
public class PostTagController {

    private final PostRepository posts;
    private final PostTagService postTagService;

    public PostTagController(PostRepository posts, PostTagService postTagService) {
        this.posts = posts;
        this.postTagService = postTagService;
    }

    @GetMapping
    public List<Tag> list(@PathVariable UUID workspaceId, @PathVariable UUID postId) {
        return postTagService.tagsForPost(postId);
    }

    @PutMapping
    public List<Tag> replace(@PathVariable UUID workspaceId, @PathVariable UUID postId, @RequestBody TagNamesRequest request) {
        Post post = posts.findById(postId).orElseThrow();
        postTagService.replaceTags(workspaceId, post, request.names());
        return postTagService.tagsForPost(postId);
    }

    public record TagNamesRequest(List<String> names) {
    }
}
