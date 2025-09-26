package com.gom.memories_diary.services;

import com.gom.memories_diary.dto.CreateMemoryDTO;
import com.gom.memories_diary.dto.MemoryResponseDTO;
import com.gom.memories_diary.model.Memory;
import com.gom.memories_diary.model.User;
import com.gom.memories_diary.repositories.MemoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MemoryService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private final MemoryRepository memoryRepository;
    private final MemorySecurity memorySecurity;
    private final UserService userService;

    public MemoryService(MemoryRepository memoryRepository, MemorySecurity memorySecurity, UserService userService) {
        this.memoryRepository = memoryRepository;
        this.memorySecurity = memorySecurity;
        this.userService = userService;
    }

    @PreAuthorize("@memorySecurity.isOwner(#id)")
    public MemoryResponseDTO findMemoryById(Long memoryId) {
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() -> new RuntimeException("Memory not found"));

        return MemoryResponseDTO.fromEntity(memory);
    }

    public Page<MemoryResponseDTO> getAllUserMemories(int page, int size) {
        Long ownerId = userService.getUserId();
        Pageable pageable = PageRequest.of(page, size);

        return memoryRepository.findAllByOwnerId(ownerId, pageable)
                .map(MemoryResponseDTO::fromEntity);
    }

    @Transactional
    @PreAuthorize("@memorySecurity.isOwner(#id)")
    public MemoryResponseDTO createMemory(CreateMemoryDTO dto, List<MultipartFile> photos) {
        Long userId = userService.getUserId();

        User user = new User();
        user.setId(userId);

        Memory newMemory = new Memory(dto, user);

        if (photos != null && !photos.isEmpty()) {
            List<String> photoUrls = savePhotos(photos);
            newMemory.setPhotoUrls(photoUrls);
        }

        Memory saved = memoryRepository.save(newMemory);

        return MemoryResponseDTO.fromEntity(saved);
    }

    @Transactional
    private List<String> savePhotos(List<MultipartFile> photos) {
        List<String> photoUrls = new ArrayList<>();

        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                try {
                    File directory = new File(uploadDir);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);
                    Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    photoUrls.add("/uploads/" + fileName);
                } catch (IOException e) {
                    throw new RuntimeException("Error when saving photo: " + e.getMessage(), e);
                }
            }
        }
        return photoUrls;
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
