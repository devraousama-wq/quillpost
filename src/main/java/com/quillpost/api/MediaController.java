package com.quillpost.api;

import com.quillpost.content.domain.MediaAsset;
import com.quillpost.media.MediaLibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces/{workspaceId}/media")
public class MediaController {

    private final MediaLibraryService mediaLibraryService;

    public MediaController(MediaLibraryService mediaLibraryService) {
        this.mediaLibraryService = mediaLibraryService;
    }

    @PostMapping
    public ResponseEntity<MediaAsset> upload(
        @PathVariable UUID workspaceId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "altText", required = false) String altText) throws IOException {
        return ResponseEntity.ok(mediaLibraryService.upload(workspaceId, file, altText));
    }
}
