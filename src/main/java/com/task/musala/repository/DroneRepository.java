package com.task.musala.repository;

import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<DroneEntity, String> {
    List<DroneEntity> findByState(DroneState state);
}

