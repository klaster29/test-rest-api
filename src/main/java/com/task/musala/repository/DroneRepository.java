package com.task.musala.repository;

import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<DroneEntity, String> {
    List<DroneEntity> findByState(DroneState state);
    Optional<DroneEntity> findBySerialNumber(String droneSerialNumber);
}

