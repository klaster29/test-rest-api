package com.task.musala.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DroneEntityTest {

    private DroneEntity drone;

    @BeforeEach
    void setUp() {
        drone = new DroneEntity("1234", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.IDLE, new ArrayList<MedicationEntity>());
    }

    @Test
    void testLoadMedications() {
        List<MedicationEntity> medications = new ArrayList<>();
        MedicationEntity medication1 = new MedicationEntity("med1", 200, "code1", new byte[] { 1, 2, 3 });
        MedicationEntity medication2 = new MedicationEntity("med2", 150, "code2", new byte[] { 4, 5, 6 });
        medications.add(medication1);
        medications.add(medication2);

        drone.loadMedications(medications);

        assertEquals(medications, drone.getLoadedMedications());
        assertEquals(DroneState.LOADED, drone.getState());
    }

    @Test
    void testLoadMedicationsWithExceededWeightLimit() {
        List<MedicationEntity> medications = new ArrayList<>();
        MedicationEntity medication1 = new MedicationEntity("med1", 400, "code1", new byte[] { 1, 2, 3 });
        MedicationEntity medication2 = new MedicationEntity("med2", 150, "code2", new byte[] { 4, 5, 6 });
        medications.add(medication1);
        medications.add(medication2);

        assertThrows(IllegalArgumentException.class, () -> {
            drone.loadMedications(medications);
        });
    }

    @Test
    void testGetBatteryLevel() {
        assertEquals(100, drone.getBatteryLevel());
    }
}
