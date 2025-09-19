package com.gom.memories_diary.controllers;

import com.gom.memories_diary.dto.CreateMemoryDTO;
import com.gom.memories_diary.dto.MemoryResponseDTO;
import com.gom.memories_diary.model.Memory;
import com.gom.memories_diary.services.MemoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memories")
public class MemoryController {

    private final MemoryService memoryService;

    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    @GetMapping("/my-memories")
    public ResponseEntity<Page<MemoryResponseDTO>> getUserMemmories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(memoryService.getAllUserMemories(page,size));
    }

    @PostMapping
    public ResponseEntity<MemoryResponseDTO> createMemory(@RequestBody CreateMemoryDTO dto) {
        MemoryResponseDTO newMemory = memoryService.createMemory(dto);
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
