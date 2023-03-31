package com.task.musala.repository;

import com.task.musala.entity.MedicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicationRepository extends JpaRepository<MedicationEntity, Long> {
    Optional<MedicationEntity> findByName(String name);
}
