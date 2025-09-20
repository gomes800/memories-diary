package com.gom.memories_diary.services;

import com.gom.memories_diary.dto.CreateMemoryDTO;
import com.gom.memories_diary.dto.MemoryResponseDTO;
import com.gom.memories_diary.model.Memory;
import com.gom.memories_diary.model.User;
import com.gom.memories_diary.repositories.MemoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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

    public Page<MemoryResponseDTO> getAllUserMemories(int page, int size) {
        Long ownerId = userService.getUserId();
        Pageable pageable = PageRequest.of(page, size);

        return memoryRepository.findAllByOwnerId(ownerId, pageable)
                .map(MemoryResponseDTO::fromEntity);
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

    @Transactional
    @PreAuthorize("@memorySecurity.isOwner(#id)")
    public MemoryResponseDTO updateMemory(Long id, CreateMemoryDTO dto) {
        Memory memory = memoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Memory not found."));

        if (dto.getTitle() != null) memory.setTitle(dto.getTitle());
        if (dto.getContent() != null) memory.setContent(dto.getContent());
        if (dto.getDate() != null) memory.setDate(dto.getDate());

        memoryRepository.save(memory);

        return MemoryResponseDTO.fromEntity(memory);
    }

    @Transactional
    @PreAuthorize("@memorySecurity.isOwner(#id)")
    public void deleteMemory(Long id) {
        Memory memory = memoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Memory not found"));

        memoryRepository.delete(memory);
    }
}
