package com.gom.memories_diary.dto;

import com.gom.memories_diary.model.Memory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoryResponseDTO {

    private Long id;
    private String title;
    private LocalDateTime date;
    private String content;
    private Long ownerId;
    private List<String> photoUrls;

    public static MemoryResponseDTO fromEntity(Memory memory) {
        return MemoryResponseDTO.builder()
                .id(memory.getId())
                .title(memory.getTitle())
                .date(memory.getDate())
                .content(memory.getContent())
                .ownerId(memory.getOwner() != null ? memory.getOwner().getId() : null)
                .photoUrls(memory.getPhotoUrls())
                .build();
    }
}
