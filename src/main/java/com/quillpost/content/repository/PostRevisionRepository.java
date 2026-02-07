package com.quillpost.content.repository;

import com.quillpost.content.domain.PostRevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRevisionRepository extends JpaRepository<PostRevision, UUID> {

    List<PostRevision> findByPostIdOrderByCreatedAtDesc(UUID postId);
}
