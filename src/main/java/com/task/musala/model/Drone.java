package com.task.musala.model;

import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.DroneModel;

public class Drone {
    private String serialNumber;
    private DroneModel model;
    private int batteryCapacity;

    public Drone(String serialNumber, DroneModel model, int batteryCapacity) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.batteryCapacity = batteryCapacity;
    }

    public static Drone fromDto(DroneEntity entity) {
        return new Drone(entity.getSerialNumber(), entity.getModel(), entity.getBatteryCapacity());
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

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "serialNumber='" + serialNumber + '\'' +
                ", model=" + model +
                ", batteryCapacity=" + batteryCapacity +
                '}';
    }
}

