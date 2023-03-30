package com.task.musala.service;

import com.task.musala.exceptions.MedicationWeightExceededException;
import com.task.musala.exceptions.NotFoundException;
import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.DroneState;
import com.task.musala.entity.MedicationEntity;
import com.task.musala.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    @Autowired
    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public void registerDrone(DroneEntity drone) throws IllegalStateException, DuplicateKeyException{
        if (drone.getWeightLimit() > 500.0) {
            throw new IllegalArgumentException("Weight limit must be 500 or less");
        }
        if (droneRepository.findById(drone.getSerialNumber()).isPresent()) {
            throw new DuplicateKeyException("Drone with that serial number already exists");
        }
        droneRepository.save(drone);
    }

    public void loadDrone(String droneSerialNumber, List<MedicationEntity> medications)
            throws IllegalArgumentException, IllegalStateException, NotFoundException {

        checkConstraints(medications);
        Optional<DroneEntity> optionalDrone = droneRepository.findById(droneSerialNumber);
        if (optionalDrone.isPresent()) {
            DroneEntity drone = optionalDrone.get();
            if (drone.getBatteryLevel() < 25 && drone.getState() == DroneState.IDLE) {
                throw new IllegalStateException("Drone battery level is too low to start loading");
            }
            double totalWeight = medications.stream().mapToDouble(MedicationEntity::getWeight).sum();
            double weightLimit = 500.0;
            if (totalWeight > weightLimit) {
                throw new MedicationWeightExceededException("Exceeded maximum weight limit");
            }
            drone.loadMedications(medications);
            droneRepository.save(drone);
        } else {
            throw new NotFoundException("Drone not found");
        }
    }

    public List<MedicationEntity> getLoadedMedications(String droneSerialNumber) {
        Optional<DroneEntity> optionalDrone = droneRepository.findById(droneSerialNumber);
        if (optionalDrone.isPresent()) {
            return optionalDrone.get().getLoadedMedications();
        } else {
            throw new NotFoundException("Drone not found");
        }
    }

    public List<DroneEntity> getAvailableDronesForLoading() {
        return droneRepository.findByState(DroneState.IDLE);
    }

    public int getDroneBatteryLevel(String droneSerialNumber) {
        Optional<DroneEntity> optionalDrone = droneRepository.findById(droneSerialNumber);
        if (optionalDrone.isPresent()) {
            return optionalDrone.get().getBatteryCapacity();
        } else {
            throw new NotFoundException("Drone not found");
        }
    }

    public List<DroneEntity> getLoadedDrones() {
        List<DroneEntity> loadedDrones = new ArrayList<>();
        for (DroneEntity drone : droneRepository.findAll()) {
            if (drone.getState() == DroneState.LOADED) {
                loadedDrones.add(drone);
            }
        }
        return loadedDrones;
    }

    private void checkConstraints(List<MedicationEntity> medications) throws IllegalStateException{
        for (MedicationEntity entity : medications) {
            if (!entity.getCode().matches("^[A-Z_\\d]+$")) {
                throw new IllegalArgumentException("you could use only upper case letters, underscore and numbers");
            }
            if (!entity.getName().matches("^[\\w\\-]+$")) {
                throw new IllegalArgumentException("You could use only letters, numbers, '-' and ''");
            }
        }
    }

}

