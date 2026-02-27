package com.quillpost.content.service;

import com.quillpost.content.domain.Tag;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.repository.TagRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TagService {

    private final TagRepository tags;
    private final WorkspaceService workspaceService;

    public TagService(TagRepository tags, WorkspaceService workspaceService) {
        this.tags = tags;
        this.workspaceService = workspaceService;
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canAuthor(#workspaceId, authentication)")
    public Tag findOrCreate(UUID workspaceId, String name) {
        Workspace workspace = workspaceService.requireWorkspace(workspaceId);
        String slug = name.toLowerCase().replaceAll("\\s+", "-");
        return tags.findByWorkspaceIdAndSlug(workspaceId, slug)
            .orElseGet(() -> tags.save(new Tag(workspace, name, slug)));
    }

    public List<Tag> autocomplete(UUID workspaceId, String query) {
        return tags.findByWorkspaceIdAndNameContainingIgnoreCaseOrderByNameAsc(workspaceId, query);
    }
}
