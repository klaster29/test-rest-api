package com.task.musala.model;

import com.task.musala.entity.MedicationEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;


@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    private String name;
    private double weight;
    private String code;
    private byte[] image;

    public static Medication fromDto(MedicationEntity entity) {
        return new Medication(entity.getName(), entity.getWeight(), entity.getCode(), entity.getImage());
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

