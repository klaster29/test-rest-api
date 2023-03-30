package com.task.musala.scheduler;

import com.task.musala.entity.AuditEventLog;
import com.task.musala.entity.DroneEntity;
import com.task.musala.repository.AuditEventLogRepository;
import com.task.musala.repository.DroneRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DroneBatteryChecker {

    private final DroneRepository droneRepository;
    private final AuditEventLogRepository auditEventLogRepository;

    public DroneBatteryChecker(DroneRepository droneRepository, AuditEventLogRepository auditEventLogRepository) {
        this.droneRepository = droneRepository;
        this.auditEventLogRepository = auditEventLogRepository;
    }

    @Scheduled(fixedRate = 15000)
    public void checkBatteryLevels() {
        List<DroneEntity> drones = droneRepository.findAll();
        for (DroneEntity drone : drones) {
            int batteryLevel = drone.getBatteryCapacity();
            AuditEventLog log = new AuditEventLog(LocalDateTime.now(),
                    "Drone battery level: " + batteryLevel + "%",
                    drone.getSerialNumber());
            auditEventLogRepository.save(log);
        }
    }
}