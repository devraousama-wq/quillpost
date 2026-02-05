package com.quillpost.content.domain;

import java.util.Objects;
import java.util.regex.Pattern;

public record Slug(String value) {

    private static final Pattern SLUG_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

    public Slug {
        Objects.requireNonNull(value, "slug required");
        if (value.isBlank() || !SLUG_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("invalid slug: " + value);
        }
    }

    public static Slug fromTitle(String title) {
        String normalized = title.toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .trim()
            .replaceAll("\\s+", "-");
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("title produces empty slug");
        }
        return new Slug(normalized);
    }
}
