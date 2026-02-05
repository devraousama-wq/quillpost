package com.quillpost.content.domain;

import java.util.Objects;

public record Excerpt(String value) {

    private static final int MAX_LENGTH = 500;

    public Excerpt {
        Objects.requireNonNull(value, "excerpt required");
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("excerpt too long");
        }
    }

    public static Excerpt of(String text) {
        if (text == null || text.isBlank()) {
            return new Excerpt("");
        }
        String trimmed = text.strip();
        if (trimmed.length() <= MAX_LENGTH) {
            return new Excerpt(trimmed);
        }
        return new Excerpt(trimmed.substring(0, MAX_LENGTH - 3) + "...");
    }
}
