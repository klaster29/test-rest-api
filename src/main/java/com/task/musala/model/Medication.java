package com.task.musala.model;

import com.task.musala.entity.MedicationEntity;

import java.util.Arrays;


public class Medication {
    private String name;
    private double weight;
    private String code;
    private byte[] image;

    public Medication(String name, double weight, String code, byte[] image) {
        this.name = name;
        this.weight = weight;
        this.code = code;
        this.image = image;
    }

    public static Medication fromDto(MedicationEntity entity) {
        return new Medication(entity.getName(), entity.getWeight(), entity.getCode(), entity.getImage());
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

    @Override
    public String toString() {
        return "Medication{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", code='" + code + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}

