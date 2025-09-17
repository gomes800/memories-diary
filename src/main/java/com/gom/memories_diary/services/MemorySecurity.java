package com.gom.memories_diary.services;

import com.gom.memories_diary.repositories.MemoryRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class MemorySecurity {

    private final MemoryRepository memoryRepository;
    private final UserService userService;


    public MemorySecurity(MemoryRepository memoryRepository, UserService userService) {
        this.memoryRepository = memoryRepository;
        this.userService = userService;
    }

    public boolean isOwner(Long memoryId) throws AccessDeniedException {
        Long userId = userService.getUserId();
        boolean exists =  memoryRepository.existsByIdAndOwnerId(memoryId, userId);

        if (!exists) {
            throw new AccessDeniedException("You dont have permission to access this memory.");
        }

        return true;
    }
}
