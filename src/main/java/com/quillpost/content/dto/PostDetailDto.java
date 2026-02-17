package com.quillpost.content.dto;

import com.quillpost.content.domain.PostStatus;

import java.time.Instant;
import java.util.UUID;

public record PostDetailDto(
    UUID id,
    String title,
    String slug,
    PostStatus status,
    String bodyMarkdown,
    String bodyHtml,
    String excerpt,
    int readingTimeMinutes,
    Instant publishAt,
    Instant updatedAt
) {
}
