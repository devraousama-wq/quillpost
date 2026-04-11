package com.quillpost.comments;

import com.quillpost.content.domain.Comment;
import com.quillpost.content.domain.Post;
import com.quillpost.content.repository.CommentRepository;
import com.quillpost.content.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CommentService {

    private static final int MAX_THREAD_DEPTH = 2;
    private static final Set<String> BLOCKED_DOMAINS = Set.of("mailinator.com", "tempmail.dev");

    private final CommentRepository comments;
    private final PostRepository posts;

    public CommentService(CommentRepository comments, PostRepository posts) {
        this.comments = comments;
        this.posts = posts;
    }

    @Transactional
    public Comment submit(UUID postId, String authorEmail, String authorName, String bodyMarkdown, UUID parentId) {
        Post post = posts.findById(postId).orElseThrow(() -> new IllegalArgumentException("post not found"));
        if (isBlocked(authorEmail)) {
            Comment blocked = new Comment(post, authorEmail, authorName, bodyMarkdown);
            blocked.setStatus(Comment.CommentStatus.BLOCKED.name());
            return comments.save(blocked);
        }
        if (bodyMarkdown.contains("http://spam")) {
            Comment comment = new Comment(post, authorEmail, authorName, bodyMarkdown);
            comment.setStatus(Comment.CommentStatus.SPAM.name());
            return comments.save(comment);
        }
        Comment comment = new Comment(post, authorEmail, authorName, bodyMarkdown);
        if (parentId != null) {
            Comment parent = comments.findById(parentId).orElseThrow();
            if (threadDepth(parent) >= MAX_THREAD_DEPTH) {
                throw new IllegalStateException("thread depth exceeded");
            }
            comment.setParent(parent);
        }
        return comments.save(comment);
    }

    public List<Comment> moderationQueue(UUID workspaceId) {
        return comments.findByPost_Workspace_IdAndStatusOrderByCreatedAtAsc(workspaceId, Comment.CommentStatus.PENDING.name());
    }

    public List<Comment> approvedForPost(UUID postId) {
        return comments.findByPostIdAndStatusOrderByCreatedAtAsc(postId, Comment.CommentStatus.APPROVED.name());
    }

    @Transactional
    public Comment approve(UUID commentId) {
        Comment comment = comments.findById(commentId).orElseThrow();
        comment.setStatus(Comment.CommentStatus.APPROVED.name());
        return comments.save(comment);
    }

    private boolean isBlocked(String email) {
        int at = email.lastIndexOf('@');
        if (at < 0) return false;
        return BLOCKED_DOMAINS.contains(email.substring(at + 1).toLowerCase());
    }

    private int threadDepth(Comment comment) {
        int depth = 0;
        Comment current = comment;
        while (current.getParent() != null && depth < MAX_THREAD_DEPTH + 1) {
            depth++;
            current = current.getParent();
        }
        return depth;
    }
}
