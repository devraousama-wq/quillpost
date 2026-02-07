package com.quillpost.content.repository;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findByWorkspaceIdAndStatusOrderByUpdatedAtDesc(UUID workspaceId, PostStatus status);

    Optional<Post> findByWorkspaceIdAndSlug(UUID workspaceId, String slug);

    List<Post> findByStatusAndPublishAtLessThanEqual(PostStatus status, Instant publishAt);
}
