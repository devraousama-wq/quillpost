package com.quillpost.api;

import com.quillpost.content.domain.MediaAsset;
import com.quillpost.media.MediaQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/media")
public class MediaListController {

    private final MediaQueryService mediaQueryService;

    public MediaListController(MediaQueryService mediaQueryService) {
        this.mediaQueryService = mediaQueryService;
    }

    @GetMapping("/list")
    public List<MediaAsset> list(@PathVariable UUID workspaceId) {
        return mediaQueryService.list(workspaceId);
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> delete(@PathVariable UUID workspaceId, @PathVariable UUID mediaId) throws IOException {
        mediaQueryService.delete(workspaceId, mediaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usage")
    public Map<String, Long> usage(@PathVariable UUID workspaceId) {
        return Map.of("bytes", mediaQueryService.diskUsageBytes(workspaceId));
    }
}
