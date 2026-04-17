package com.quillpost.content.repository;

import com.quillpost.content.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByPost_Workspace_IdAndStatusOrderByCreatedAtAsc(UUID workspaceId, String status);

    List<Comment> findByPostIdAndStatusOrderByCreatedAtAsc(UUID postId, String status);
}
