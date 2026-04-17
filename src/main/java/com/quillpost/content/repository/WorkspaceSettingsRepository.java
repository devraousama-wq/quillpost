package com.quillpost.content.repository;

import com.quillpost.content.domain.WorkspaceSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkspaceSettingsRepository extends JpaRepository<WorkspaceSettings, UUID> {
}
