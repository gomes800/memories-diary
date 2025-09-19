package com.gom.memories_diary.repositories;

import com.gom.memories_diary.model.Memory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {

    boolean existsByIdAndOwnerId(Long id, Long ownerId);

    List<Memory> findAllByOwnerId(Long userId);

    Page<Memory> findAllByOwnerId(Long userId, Pageable pageable);
}
