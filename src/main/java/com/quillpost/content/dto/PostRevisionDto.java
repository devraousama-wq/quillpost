package com.quillpost.content.dto;

import java.time.Instant;
import java.util.UUID;

public record PostRevisionDto(
    UUID id,
    UUID postId,
    UUID authorId,
    String title,
    String bodyMarkdown,
    Instant createdAt
) {
}
