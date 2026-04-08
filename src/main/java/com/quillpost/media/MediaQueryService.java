package com.quillpost.media;

import com.quillpost.content.domain.MediaAsset;
import com.quillpost.content.repository.MediaAssetRepository;
import com.quillpost.content.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class MediaQueryService {

    private final MediaAssetRepository mediaAssets;
    private final WorkspaceService workspaceService;
    private final Path storageRoot;

    public MediaQueryService(
        MediaAssetRepository mediaAssets,
        WorkspaceService workspaceService,
        @Value("${quillpost.media.storage-path}") String storagePath) {
        this.mediaAssets = mediaAssets;
        this.workspaceService = workspaceService;
        this.storageRoot = Path.of(storagePath);
    }

    public List<MediaAsset> list(UUID workspaceId) {
        return mediaAssets.findByWorkspaceIdOrderByUploadedAtDesc(workspaceId);
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canManage(#workspaceId, authentication)")
    public void delete(UUID workspaceId, UUID mediaId) throws IOException {
        workspaceService.requireWorkspace(workspaceId);
        MediaAsset asset = mediaAssets.findById(mediaId).orElseThrow();
        if (!asset.getWorkspace().getId().equals(workspaceId)) {
            throw new IllegalArgumentException("workspace mismatch");
        }
        Path dir = storageRoot.resolve(workspaceId.toString());
        Files.deleteIfExists(dir.resolve(asset.getId() + "-original"));
        Files.deleteIfExists(dir.resolve(asset.getId() + "-thumb.jpg"));
        mediaAssets.delete(asset);
    }

    public long diskUsageBytes(UUID workspaceId) {
        Path dir = storageRoot.resolve(workspaceId.toString());
        if (!Files.exists(dir)) {
            return 0;
        }
        try (var stream = Files.walk(dir)) {
            return stream.filter(Files::isRegularFile).mapToLong(p -> {
                try {
                    return Files.size(p);
                } catch (IOException e) {
                    return 0;
                }
            }).sum();
        } catch (IOException e) {
            return 0;
        }
    }
}
