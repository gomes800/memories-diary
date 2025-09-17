package com.gom.memories_diary.services;

import com.gom.memories_diary.dto.CreateMemoryDTO;
import com.gom.memories_diary.dto.MemoryResponseDTO;
import com.gom.memories_diary.model.Memory;
import com.gom.memories_diary.model.User;
import com.gom.memories_diary.repositories.MemoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoryService {

    private final MemoryRepository memoryRepository;
    private final MemorySecurity memorySecurity;
    private final UserService userService;

    public MemoryService(MemoryRepository memoryRepository, MemorySecurity memorySecurity, UserService userService) {
        this.memoryRepository = memoryRepository;
        this.memorySecurity = memorySecurity;
        this.userService = userService;
    }

    public List<Memory> getAll() {
        List<Memory> memories = memoryRepository.findAll();

        return memories;
    }

    @Transactional
    @PreAuthorize("@memorySecurity.isOwner(#id)")
    public MemoryResponseDTO createMemory(CreateMemoryDTO dto) {
        Long userId = userService.getUserId();

        User user = new User();
        user.setId(userId);

        Memory newMemory = new Memory(dto, user);

        Memory saved = memoryRepository.save(newMemory);

        return MemoryResponseDTO.fromEntity(saved);
    }
}
