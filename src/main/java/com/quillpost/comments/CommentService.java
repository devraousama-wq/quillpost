package com.quillpost.comments;

import com.quillpost.content.domain.Comment;
import com.quillpost.content.domain.Post;
import com.quillpost.content.repository.CommentRepository;
import com.quillpost.content.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    private final CommentRepository comments;
    private final PostRepository posts;

    public CommentService(CommentRepository comments, PostRepository posts) {
        this.comments = comments;
        this.posts = posts;
    }

    @Transactional
    public Comment submit(UUID postId, String authorEmail, String authorName, String bodyMarkdown) {
        Post post = posts.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));
        if (bodyMarkdown.contains("http://spam")) {
            Comment comment = new Comment(post, authorEmail, authorName, bodyMarkdown);
            comment.setStatus(Comment.CommentStatus.SPAM.name());
            return comments.save(comment);
        }
        return comments.save(new Comment(post, authorEmail, authorName, bodyMarkdown));
    }

    public List<Comment> moderationQueue(UUID workspaceId) {
        return comments.findByPost_Workspace_IdAndStatusOrderByCreatedAtAsc(workspaceId, Comment.CommentStatus.PENDING.name());
    }

    @Transactional
    public Comment approve(UUID commentId) {
        Comment comment = comments.findById(commentId).orElseThrow();
        comment.setStatus(Comment.CommentStatus.APPROVED.name());
        return comments.save(comment);
    }
}
