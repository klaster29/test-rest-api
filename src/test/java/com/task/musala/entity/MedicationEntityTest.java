package com.task.musala.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MedicationEntityTest {

    @Test
    public void testSetName() {
        MedicationEntity medication = new MedicationEntity();
        medication.setName("Paracetamol");
        assertEquals("Paracetamol", medication.getName());
    }

    @Test
    public void testSetWeight() {
        MedicationEntity medication = new MedicationEntity();
        medication.setWeight(10.5);
        assertEquals(10.5, medication.getWeight(), 0.01);
    }

    @Test
    public void testSetCode() {
        MedicationEntity medication = new MedicationEntity();
        medication.setCode("CODE_123");
        assertEquals("CODE_123", medication.getCode());
    }

    @Test
    public void testSetImage() {
        MedicationEntity medication = new MedicationEntity();
        byte[] image = new byte[] {1, 2, 3};
        medication.setImage(image);
        assertArrayEquals(image, medication.getImage());
    }

    @Test
    public void testSetDroneEntity() {
        MedicationEntity medication = new MedicationEntity();
        DroneEntity drone = new DroneEntity();
        medication.setDroneEntity(drone);
        assertEquals(drone, medication.getDroneEntity());
    }
}