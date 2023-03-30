package com.task.musala.model;

import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.DroneModel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Drone {
    private String serialNumber;
    private DroneModel model;
    private int batteryCapacity;

    public static Drone fromDto(DroneEntity entity) {
        return new Drone(entity.getSerialNumber(), entity.getModel(), entity.getBatteryCapacity());
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

