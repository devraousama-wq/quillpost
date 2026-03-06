package com.quillpost.media;

import com.quillpost.content.domain.MediaAsset;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.repository.MediaAssetRepository;
import com.quillpost.content.service.WorkspaceService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@Service
public class MediaLibraryService {

    private static final Set<String> ALLOWED = Set.of("image/jpeg", "image/png", "image/webp", "image/gif");

    private final MediaAssetRepository mediaAssets;
    private final WorkspaceService workspaceService;
    private final Path storageRoot;

    public MediaLibraryService(
        MediaAssetRepository mediaAssets,
        WorkspaceService workspaceService,
        @Value("${quillpost.media.storage-path}") String storagePath) {
        this.mediaAssets = mediaAssets;
        this.workspaceService = workspaceService;
        this.storageRoot = Path.of(storagePath);
    }

    @Transactional
    @PreAuthorize("@workspaceAccess.canAuthor(#workspaceId, authentication)")
    public MediaAsset upload(UUID workspaceId, MultipartFile file, String altText) throws IOException {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("file too large");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED.contains(contentType)) {
            throw new IllegalArgumentException("mime type not allowed");
        }
        Workspace workspace = workspaceService.requireWorkspace(workspaceId);
        MediaAsset asset = new MediaAsset(
            workspace,
            file.getOriginalFilename(),
            contentType,
            MediaAsset.MediaType.IMAGE,
            file.getSize());
        asset.setAltText(altText);
        mediaAssets.save(asset);

        Path workspaceDir = storageRoot.resolve(workspaceId.toString());
        Files.createDirectories(workspaceDir);
        Path original = workspaceDir.resolve(asset.getId() + "-original");
        file.transferTo(original);
        Path thumb = workspaceDir.resolve(asset.getId() + "-thumb.jpg");
        Thumbnails.of(original.toFile()).size(320, 320).toFile(thumb.toFile());
        return asset;
    }
}
