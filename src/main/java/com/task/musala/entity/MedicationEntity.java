package com.task.musala.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.Arrays;

@Entity
public class MedicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^[\\w\\-]+$", message = "Only letters, numbers, '-' and '' are allowed")
    private String name;
    private double weight;
    @Pattern(regexp = "^[A-Z_\\d]+$", message = "Only upper case letters, underscore and numbers are allowed")
    private String code;
    @Lob
    private byte[] image;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_entity_serial_number")
    private DroneEntity droneEntity;

    public MedicationEntity() {
    }

    public MedicationEntity(String name, double weight, String code, byte[] image) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public DroneEntity getDroneEntity() {
        return droneEntity;
    }

    public void setDroneEntity(DroneEntity droneEntity) {
        this.droneEntity = droneEntity;
    }

    @Override
    public String toString() {
        return "MedicationEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", code='" + code + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
