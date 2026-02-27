package com.quillpost.content.service;

import com.quillpost.content.domain.Category;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.repository.CategoryRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TaxonomyService {

    private final CategoryRepository categories;
    private final WorkspaceService workspaceService;

    public TaxonomyService(CategoryRepository categories, WorkspaceService workspaceService) {
        this.categories = categories;
        this.workspaceService = workspaceService;
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canManage(#workspaceId, authentication)")
    public Category createCategory(UUID workspaceId, String name, String slug, UUID parentId) {
        Workspace workspace = workspaceService.requireWorkspace(workspaceId);
        categories.findByWorkspaceIdAndSlug(workspaceId, slug).ifPresent(c -> {
            throw new IllegalArgumentException("category slug taken");
        });
        Category category = new Category(workspace, name, slug);
        if (parentId != null) {
            Category parent = categories.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("parent not found"));
            if (parent.getParent() != null) {
                throw new IllegalArgumentException("category depth exceeded");
            }
            category.setParent(parent);
        }
        return categories.save(category);
    }

    public List<Category> listCategories(UUID workspaceId) {
        return categories.findByWorkspaceIdOrderByNameAsc(workspaceId);
    }
}
