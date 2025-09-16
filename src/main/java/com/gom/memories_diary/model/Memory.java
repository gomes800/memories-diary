package com.gom.memories_diary.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "memories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Memory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime date;
    private String content;

    @ElementCollection
    @CollectionTable(name = "memory_photos", joinColumns = @JoinColumn(name = "memory_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

}
