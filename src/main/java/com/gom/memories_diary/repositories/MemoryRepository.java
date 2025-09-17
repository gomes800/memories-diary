package com.gom.memories_diary.repositories;

import com.gom.memories_diary.model.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {

    boolean existsByIdAndOwnerId(Long id, Long ownerId);
}
