package com.task.musala.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DroneMedicationId implements Serializable {

    @Column(name = "drone_serial_number")
    private String droneSerialNumber;

    @Column(name = "medication_id")
    private Long medicationId;

    public String getDroneSerialNumber() {
        return droneSerialNumber;
    }

    public void setDroneSerialNumber(String droneSerialNumber) {
        this.droneSerialNumber = droneSerialNumber;
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Long medicationId) {
        this.medicationId = medicationId;
    }

    public DroneMedicationId() {
    }

    public DroneMedicationId(String droneSerialNumber, Long medicationId) {
        this.droneSerialNumber = droneSerialNumber;
        this.medicationId = medicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DroneMedicationId that = (DroneMedicationId) o;
        return droneSerialNumber.equals(that.droneSerialNumber) && medicationId.equals(that.medicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(droneSerialNumber, medicationId);
    }
}
