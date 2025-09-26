package com.gom.memories_diary.controllers;

import com.gom.memories_diary.dto.CreateMemoryDTO;
import com.gom.memories_diary.dto.MemoryResponseDTO;
import com.gom.memories_diary.services.MemoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/memories")
public class MemoryController {

    private final MemoryService memoryService;

    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoryResponseDTO> getMemoryById(@PathVariable Long id) {
        return ResponseEntity.ok(memoryService.findMemoryById(id));
    }

    @GetMapping("/my-memories")
    public ResponseEntity<Page<MemoryResponseDTO>> getUserMemmories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(memoryService.getAllUserMemories(page,size));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemoryResponseDTO> createMemory(
            @RequestPart("memory") CreateMemoryDTO dto,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos
            ) {
        MemoryResponseDTO newMemory = memoryService.createMemory(dto, photos);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newMemory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MemoryResponseDTO> updateMemory(
            @PathVariable Long id,
            @RequestBody CreateMemoryDTO dto
    ) {
        MemoryResponseDTO updated = memoryService.updateMemory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemory(@PathVariable Long id) {
        memoryService.deleteMemory(id);
        return ResponseEntity.noContent().build();
    }
}
