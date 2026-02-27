package com.quillpost.content.repository;

import com.quillpost.content.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByWorkspaceIdOrderByNameAsc(UUID workspaceId);

    Optional<Category> findByWorkspaceIdAndSlug(UUID workspaceId, String slug);
}
