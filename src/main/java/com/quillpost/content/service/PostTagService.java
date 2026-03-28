package com.quillpost.content.service;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostTag;
import com.quillpost.content.domain.Tag;
import com.quillpost.content.repository.PostTagRepository;
import com.quillpost.content.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PostTagService {

    private final PostTagRepository postTags;
    private final TagRepository tags;
    private final TagService tagService;

    public PostTagService(PostTagRepository postTags, TagRepository tags, TagService tagService) {
        this.postTags = postTags;
        this.tags = tags;
        this.tagService = tagService;
    }

    @Transactional
    public void replaceTags(UUID workspaceId, Post post, List<String> tagNames) {
        postTags.findAll().stream()
            .filter(pt -> pt.getPost().getId().equals(post.getId()))
            .forEach(postTags::delete);
        for (String name : tagNames) {
            Tag tag = tagService.findOrCreate(workspaceId, name);
            postTags.save(new PostTag(post, tag));
        }
    }

    public List<Tag> tagsForPost(UUID postId) {
        return postTags.findAll().stream()
            .filter(pt -> pt.getPost().getId().equals(postId))
            .map(PostTag::getTag)
            .toList();
    }

    public List<Post> postsForTag(UUID workspaceId, String tagSlug) {
        Tag tag = tags.findByWorkspaceIdAndSlug(workspaceId, tagSlug).orElseThrow();
        return postTags.findAll().stream()
            .filter(pt -> pt.getTag().getId().equals(tag.getId()))
            .map(PostTag::getPost)
            .toList();
    }
}
