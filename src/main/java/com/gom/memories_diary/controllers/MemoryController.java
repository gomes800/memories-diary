package com.gom.memories_diary.controllers;

import com.gom.memories_diary.model.Memory;
import com.gom.memories_diary.services.MemoryService;
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

    @GetMapping
    public ResponseEntity<List<Memory>> getAll() {
        List<Memory> memories = memoryService.getAll();
        return ResponseEntity.ok(memories);
    }

    @PostMapping
    public ResponseEntity<Memory> createMemory(@RequestBody Memory memory) {
        memoryService.createMemory(memory);

        return ResponseEntity.ok(memory);
    }
}
