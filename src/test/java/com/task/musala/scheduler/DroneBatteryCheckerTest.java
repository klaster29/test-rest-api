package com.task.musala.scheduler;

import com.task.musala.entity.AuditEventLog;
import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.DroneModel;
import com.task.musala.entity.DroneState;
import com.task.musala.repository.AuditEventLogRepository;
import com.task.musala.repository.DroneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DroneBatteryCheckerTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private AuditEventLogRepository auditEventLogRepository;

    @InjectMocks
    private DroneBatteryChecker droneBatteryChecker;

    @Test
    public void testCheckBatteryLevels() {
        List<DroneEntity> drones = new ArrayList<>();
        DroneEntity drone1 = new DroneEntity("123", DroneModel.CRUISERWEIGHT, 500, 100, DroneState.IDLE);
        DroneEntity drone2 = new DroneEntity("456", DroneModel.LIGHTWEIGHT, 250, 50, DroneState.IDLE);
        drones.add(drone1);
        drones.add(drone2);
        when(droneRepository.findAll()).thenReturn(drones);

        droneBatteryChecker.checkBatteryLevels();

        verify(auditEventLogRepository, times(2)).save(any(AuditEventLog.class));
    }

    @Test
    public void testCheckBatteryLevelsNoDrones() {
        List<DroneEntity> drones = new ArrayList<>();
        when(droneRepository.findAll()).thenReturn(drones);

        droneBatteryChecker.checkBatteryLevels();

        verify(auditEventLogRepository, never()).save(any(AuditEventLog.class));
    }

}
