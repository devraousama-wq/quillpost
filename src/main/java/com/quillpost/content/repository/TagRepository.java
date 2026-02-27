package com.quillpost.content.repository;

import com.quillpost.content.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    List<Tag> findByWorkspaceIdAndNameContainingIgnoreCaseOrderByNameAsc(UUID workspaceId, String query);

    Optional<Tag> findByWorkspaceIdAndSlug(UUID workspaceId, String slug);
}
