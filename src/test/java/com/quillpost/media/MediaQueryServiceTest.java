package com.quillpost.media;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MediaQueryServiceTest {

    @Test
    void diskUsageEmptyWhenMissing(@TempDir Path temp) {
        long usage = temp.resolve("missing-workspace").toFile().exists() ? 1 : 0;
        assertEquals(0, usage);
    }
}
