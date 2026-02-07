package com.quillpost.content.repository;

import com.quillpost.content.domain.WorkspaceMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceMembershipRepository extends JpaRepository<WorkspaceMembership, UUID> {

    List<WorkspaceMembership> findByUserId(UUID userId);

    Optional<WorkspaceMembership> findByWorkspaceIdAndUserId(UUID workspaceId, UUID userId);
}
