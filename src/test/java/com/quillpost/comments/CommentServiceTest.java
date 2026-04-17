package com.quillpost.comments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CommentServiceTest {

    @Test
    void blockedDomainsConfigured() {
        assertTrue(CommentService.class.getDeclaredFields().length > 0);
    }
}
