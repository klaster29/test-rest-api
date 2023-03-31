package com.task.musala.repository;

import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.DroneMedicationEntity;
import com.task.musala.entity.DroneMedicationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneMedicationRepository extends JpaRepository<DroneMedicationEntity, DroneMedicationId> {

    Optional<List<DroneMedicationEntity>> findByDroneEntity(DroneEntity droneEntity);
}

