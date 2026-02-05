package com.quillpost.content.dto;

import java.util.UUID;

public record WorkspaceDto(UUID id, String slug, String name) {
}
