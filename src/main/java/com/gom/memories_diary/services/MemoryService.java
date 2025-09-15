package com.gom.memories_diary.services;

import com.gom.memories_diary.model.Memory;
import com.gom.memories_diary.repositories.MemoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoryService {

    private final MemoryRepository memoryRepository;

    public MemoryService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    public List<Memory> getAll() {
        List<Memory> memories = memoryRepository.findAll();

        return memories;
    }

    public Memory createMemory(Memory memory) {
        return memoryRepository.save(memory);
    }
}
