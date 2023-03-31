package com.task.musala.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "drone_medication")
public class DroneMedicationEntity {

    @EmbeddedId
    private DroneMedicationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("droneSerialNumber")
    private DroneEntity droneEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicationId")
    private MedicationEntity medicationEntity;

    public DroneMedicationId getId() {
        return id;
    }

    public void setId(DroneMedicationId id) {
        this.id = id;
    }

    public DroneEntity getDroneEntity() {
        return droneEntity;
    }

    public void setDroneEntity(DroneEntity droneEntity) {
        this.droneEntity = droneEntity;
    }

    public MedicationEntity getMedicationEntity() {
        return medicationEntity;
    }

    public void setMedicationEntity(MedicationEntity medicationEntity) {
        this.medicationEntity = medicationEntity;
    }

    public DroneMedicationEntity() {
    }

    public DroneMedicationEntity(DroneMedicationId id, DroneEntity droneEntity, MedicationEntity medicationEntity) {
        this.id = id;
        this.droneEntity = droneEntity;
        this.medicationEntity = medicationEntity;
    }
}
