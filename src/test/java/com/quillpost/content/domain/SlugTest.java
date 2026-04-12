package com.quillpost.content.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SlugTest {

    @Test
    void fromTitleNormalizes() {
        assertEquals("hello-world", Slug.fromTitle("Hello World!").value());
    }

    @Test
    void rejectsInvalidSlug() {
        assertThrows(IllegalArgumentException.class, () -> new Slug("Bad Slug"));
    }
}
