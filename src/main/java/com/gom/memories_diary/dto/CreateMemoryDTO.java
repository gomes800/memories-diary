package com.gom.memories_diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemoryDTO {

    private String title;
    private LocalDate date;
    private String content;
}
