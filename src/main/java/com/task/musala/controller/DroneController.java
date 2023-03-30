package com.task.musala.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.task.musala.exceptions.DroneStateException;
import com.task.musala.exceptions.MedicationWeightExceededException;
import com.task.musala.exceptions.NotFoundException;
import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.MedicationEntity;
import com.task.musala.model.Drone;
import com.task.musala.model.Medication;
import com.task.musala.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drone-service")
public class DroneController {

    private final DroneService droneService;
    @Autowired
    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping("/register-drone")
    public ResponseEntity<String> registerDrone(@RequestBody DroneEntity drone) {
        try {
            droneService.registerDrone(drone);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicateKeyException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the drone.");
        }
    }


    @PostMapping("/load-drone")
    public ResponseEntity<?> loadDrone(@RequestParam("serialNumber") String droneSerialNumber,
                                       @RequestBody List<MedicationEntity> medications) {
        try {
            droneService.loadDrone(droneSerialNumber, medications);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (MedicationWeightExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/loaded-medications")
    public ResponseEntity<?> getLoadedMedications(@RequestParam("serialNumber") String droneSerialNumber) {
        try {
            List<Medication> loadedMedications = droneService.getLoadedMedications(droneSerialNumber).stream()
                    .map(Medication::fromDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(loadedMedications);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DroneStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    @GetMapping("/drones")
    public ResponseEntity<?> getAvailableDronesForLoadingMedications
            (@RequestParam(value = "loadedMedications", required = false) Boolean loadedMedications) {
        try {
            List<DroneEntity> drones = loadedMedications == null ||
                    !loadedMedications ? droneService.getAvailableDronesForLoading() : droneService.getLoadedDrones();
            return ResponseEntity.ok(drones.stream().map(Drone::fromDto).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get drones: " + e.getMessage());
        }
    }


    @GetMapping("/drone-battery-level")
    public ResponseEntity<?> getDroneBatteryLevel(@RequestParam("serialNumber") String droneSerialNumber) {
        try {
            int batteryLevel = droneService.getDroneBatteryLevel(droneSerialNumber);
            return ResponseEntity.ok(batteryLevel + "%");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Drone not found");
        } catch (DroneStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Drone is not loaded");
        }
    }
}