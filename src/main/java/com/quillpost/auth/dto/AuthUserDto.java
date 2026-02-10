package com.quillpost.auth.dto;

import java.util.UUID;

public record AuthUserDto(UUID id, String email, String displayName) {
}
