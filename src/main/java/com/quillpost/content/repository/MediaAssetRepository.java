package com.quillpost.content.repository;

import com.quillpost.content.domain.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, UUID> {

    List<MediaAsset> findByWorkspaceIdOrderByUploadedAtDesc(UUID workspaceId);
}
