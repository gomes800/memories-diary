package com.gom.memories_diary.controllers;

import com.gom.memories_diary.repositories.MemoryRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/memories/photos")
public class MemoryPhotoController {

    private final String uploadDir = "uploads";
    private final MemoryRepository memoryRepository;

    public MemoryPhotoController(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    @PreAuthorize("@memorySecurity.isOwner(#memoryId)")
    @GetMapping("/{memoryId}/{filename}")
    public ResponseEntity<Resource> getPhoto(
            @PathVariable Long memoryId,
            @PathVariable String filename
    ) throws Exception {

        var memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new RuntimeException("Memory not found."));

        if (!memory.getPhotoUrls().contains(filename)) {
            return ResponseEntity.status(403).build();
        }

        Path path = Paths.get(uploadDir).resolve(filename);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(path.toUri());

        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }
}