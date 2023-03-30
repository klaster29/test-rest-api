package com.task.musala.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DroneEntity {
    @Id
    @Size(max = 100)
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    private DroneModel model;
    @Max(value = 500, message = "Maximum weight limit is 500 grams")
    private double weightLimit;
    private int batteryCapacity;
    @Enumerated(EnumType.STRING)
    private DroneState state;
    @OneToMany(mappedBy = "droneEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MedicationEntity> loadedMedications;

    public DroneEntity() {
        this.loadedMedications = new ArrayList<>();
    }

    public DroneEntity(String serialNumber, DroneModel model, double weightLimit, int batteryCapacity, DroneState state, List<MedicationEntity> loadedMedications) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
        this.loadedMedications = loadedMedications;
    }

    public DroneEntity(String serialNumber, DroneModel model, double weightLimit, int batteryCapacity, DroneState state) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimit = weightLimit;
        this.batteryCapacity = batteryCapacity;
        this.state = state;
        this.loadedMedications = new ArrayList<>();
    }

    public void loadMedications(List<MedicationEntity> medications) {
        if (state != DroneState.IDLE) {
            throw new IllegalStateException("Drone is not in IDLE state");
        }
        double totalWeight = loadedMedications.stream().mapToDouble(MedicationEntity::getWeight).sum();
        double weightToAdd = medications.stream().mapToDouble(MedicationEntity::getWeight).sum();
        if (totalWeight + weightToAdd > weightLimit) {
            throw new IllegalArgumentException("Exceeded maximum weight limit");
        }
        loadedMedications.addAll(medications);
        state = DroneState.LOADED;
    }

    public int getBatteryLevel() {
        return batteryCapacity;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public DroneModel getModel() {
        return model;
    }

    public void setModel(DroneModel model) {
        this.model = model;
    }

    public double getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(double weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public DroneState getState() {
        return state;
    }

    public void setState(DroneState state) {
        this.state = state;
    }

    public List<MedicationEntity> getLoadedMedications() {
        return loadedMedications;
    }

    public void setLoadedMedications(List<MedicationEntity> loadedMedications) {
        this.loadedMedications = loadedMedications;
    }

    @Override
    public String toString() {
        return "DroneEntity{" +
                "serialNumber='" + serialNumber + '\'' +
                ", model=" + model +
                ", weightLimit=" + weightLimit +
                ", batteryCapacity=" + batteryCapacity +
                ", state=" + state +
                ", loadedMedications=" + loadedMedications +
                '}';
    }
}