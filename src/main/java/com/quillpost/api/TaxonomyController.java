package com.quillpost.api;

import com.quillpost.content.domain.Category;
import com.quillpost.content.domain.Tag;
import com.quillpost.content.service.TagService;
import com.quillpost.content.service.TaxonomyService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/taxonomies")
public class TaxonomyController {

    private final TaxonomyService taxonomyService;
    private final TagService tagService;

    public TaxonomyController(TaxonomyService taxonomyService, TagService tagService) {
        this.taxonomyService = taxonomyService;
        this.tagService = tagService;
    }

    @GetMapping("/categories")
    public List<Category> categories(@PathVariable UUID workspaceId) {
        return taxonomyService.listCategories(workspaceId);
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@PathVariable UUID workspaceId, @Valid @RequestBody CreateCategoryRequest request) {
        Category category = taxonomyService.createCategory(workspaceId, request.name(), request.slug(), request.parentId());
        return ResponseEntity.ok(category);
    }

    @GetMapping("/tags")
    public List<Tag> autocomplete(@PathVariable UUID workspaceId, @RequestParam String q) {
        return tagService.autocomplete(workspaceId, q);
    }

    @PostMapping("/tags")
    public ResponseEntity<Tag> createTag(@PathVariable UUID workspaceId, @Valid @RequestBody CreateTagRequest request) {
        return ResponseEntity.ok(tagService.findOrCreate(workspaceId, request.name()));
    }

    public record CreateCategoryRequest(@NotBlank String name, @NotBlank String slug, UUID parentId) {
    }

    public record CreateTagRequest(@NotBlank String name) {
    }
}
