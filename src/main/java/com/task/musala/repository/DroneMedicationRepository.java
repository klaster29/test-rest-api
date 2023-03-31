package com.task.musala.repository;

import com.task.musala.entity.DroneMedicationEntity;
import com.task.musala.entity.DroneMedicationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneMedicationRepository extends JpaRepository<DroneMedicationEntity, DroneMedicationId> {
}

