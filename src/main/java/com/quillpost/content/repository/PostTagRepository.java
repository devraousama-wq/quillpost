package com.quillpost.content.repository;

import com.quillpost.content.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, PostTag.PostTagId> {
}
