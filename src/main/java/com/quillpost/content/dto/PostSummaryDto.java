package com.quillpost.content.dto;

import com.quillpost.content.domain.PostStatus;

import java.time.Instant;
import java.util.UUID;

public record PostSummaryDto(
    UUID id,
    String title,
    String slug,
    PostStatus status,
    Instant publishAt,
    int readingTimeMinutes,
    Instant updatedAt
) {
}
