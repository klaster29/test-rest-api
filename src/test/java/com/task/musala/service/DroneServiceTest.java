package com.task.musala.service;

import com.task.musala.exceptions.NotFoundException;
import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.DroneModel;
import com.task.musala.entity.DroneState;
import com.task.musala.entity.MedicationEntity;
import com.task.musala.repository.DroneMedicationRepository;
import com.task.musala.repository.DroneRepository;
import com.task.musala.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;
    @Mock
    private DroneMedicationRepository droneMedicationRepository;
    @Mock
    private MedicationRepository medicationRepository;

    private DroneService droneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        droneService = new DroneService(droneRepository, droneMedicationRepository, medicationRepository);
    }

    @Test
    public void testRegisterDrone() {
        // Given
        DroneEntity drone = new DroneEntity("serialNumber", DroneModel.CRUISERWEIGHT, 500, 100, DroneState.IDLE);
        when(droneRepository.save(any(DroneEntity.class))).thenReturn(drone);

        // When
        droneService.registerDrone(drone);

        // Then
        ArgumentCaptor<DroneEntity> argument = ArgumentCaptor.forClass(DroneEntity.class);
        Mockito.verify(droneRepository).save(argument.capture());
        assertEquals(drone, argument.getValue());
    }

    @Test
    public void testLoadDrone() {
        // Given
        String serialNumber = "serialNumber";
        List<MedicationEntity> medications = new ArrayList<>();
        MedicationEntity medication1 = new MedicationEntity("med1", 200, "code1", new byte[] { 1, 2, 3 });
        MedicationEntity medication2 = new MedicationEntity("med2", 300, "code2", new byte[] { 4, 5, 6 });
        medications.add(medication1);
        medications.add(medication2);
        DroneEntity drone = new DroneEntity(serialNumber, DroneModel.LIGHTWEIGHT, 500, 100, DroneState.IDLE);
        when(droneRepository.findById(eq(serialNumber))).thenReturn(Optional.of(drone));
        when(droneRepository.save(any(DroneEntity.class))).thenReturn(drone);

        // When
        droneService.loadDrone(serialNumber, medications);

        // Then
        ArgumentCaptor<DroneEntity> argument = ArgumentCaptor.forClass(DroneEntity.class);
        Mockito.verify(droneRepository).save(argument.capture());
        assertEquals(DroneState.LOADED, argument.getValue().getState());
    }

    @Test
    void testGetLoadedMedicationsSuccess() {
        String droneSerialNumber = "ABC123";
        DroneEntity drone = new DroneEntity();
        List<MedicationEntity> medications = new ArrayList<>();
        medications.add(new MedicationEntity("Med1", 100.0, "M1", new byte[]{1, 2, 3}));
        medications.add(new MedicationEntity("Med2", 200.0, "M2", new byte[]{4, 5, 6}));
        drone.setLoadedMedications(medications);
        when(droneRepository.findById(droneSerialNumber)).thenReturn(Optional.of(drone));

        List<MedicationEntity> loadedMedications = droneService.getLoadedMedications(droneSerialNumber);

        assertEquals(medications, loadedMedications);
    }

    @Test
    void testGetLoadedMedicationsDroneNotFound() {
        String droneSerialNumber = "ABC123";
        when(droneRepository.findById(droneSerialNumber)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> droneService.getLoadedMedications(droneSerialNumber));
    }

    @Test
    void testGetAvailableDronesForLoading() {
        List<DroneEntity> drones = Arrays.asList(
                new DroneEntity("serial1", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.IDLE, new ArrayList<>()),
                new DroneEntity("serial2", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.LOADED, new ArrayList<>()),
                new DroneEntity("serial3", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.IDLE, new ArrayList<>()));
        drones.forEach(System.out::println);
        Mockito.when(droneRepository.findByState(DroneState.IDLE)).thenReturn(drones);

        List<DroneEntity> availableDrones = droneService.getAvailableDronesForLoading();
        assertEquals(3, availableDrones.size());
        availableDrones = availableDrones.stream().filter(drone -> drone.getState().equals(DroneState.IDLE)).collect(Collectors.toList());
        assertEquals(2, availableDrones.size());
        assertTrue(availableDrones.stream().allMatch(drone -> drone.getState().equals(DroneState.IDLE)));
    }

    @Test
    void testGetAvailableDronesForLoadingNoAvailableDrones() {
        Mockito.when(droneRepository.findByState(DroneState.IDLE)).thenReturn(Collections.emptyList());

        List<DroneEntity> availableDrones = droneService.getAvailableDronesForLoading();

        assertTrue(availableDrones.isEmpty());
    }

    @Test
    void testGetDroneBatteryLevelForExistingDrone() {
        String serialNumber = "123";
        int expectedBatteryLevel = 80;
        DroneEntity drone = new DroneEntity(serialNumber, DroneModel.LIGHTWEIGHT, 500, expectedBatteryLevel, DroneState.IDLE, new ArrayList<>());
        when(droneRepository.findById(serialNumber)).thenReturn(Optional.of(drone));

        int actualBatteryLevel = droneService.getDroneBatteryLevel(serialNumber);

        assertEquals(expectedBatteryLevel, actualBatteryLevel);
        Mockito.verify(droneRepository).findById(serialNumber);
    }

    @Test
    void testGetDroneBatteryLevelForNonExistingDrone() {
        String serialNumber = "456";
        when(droneRepository.findById(serialNumber)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> droneService.getDroneBatteryLevel(serialNumber));
        Mockito.verify(droneRepository).findById(serialNumber);
    }

    @Test
    void testGetLoadedDronesWhenEmpty() {
        when(droneRepository.findAll()).thenReturn(Collections.emptyList());

        List<DroneEntity> loadedDrones = droneService.getLoadedDrones();

        assertTrue(loadedDrones.isEmpty());
    }

    @Test
    void testGetLoadedDronesWithOneLoadedDrone() {
        DroneEntity loadedDrone = new DroneEntity("serial1", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.LOADED, Collections.emptyList());
        when(droneRepository.findAll()).thenReturn(Collections.singletonList(loadedDrone));

        List<DroneEntity> loadedDrones = droneService.getLoadedDrones();

        assertIterableEquals(Collections.singletonList(loadedDrone), loadedDrones);
    }

    @Test
    void testGetLoadedDronesWithOneIdleDrone() {
        DroneEntity idleDrone = new DroneEntity("serial1", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.IDLE, Collections.emptyList());
        when(droneRepository.findAll()).thenReturn(Collections.singletonList(idleDrone));

        List<DroneEntity> loadedDrones = droneService.getLoadedDrones();

        assertTrue(loadedDrones.isEmpty());
    }

    @Test
    void testGetLoadedDronesWithMultipleDrones() {
        DroneEntity loadedDrone1 = new DroneEntity("serial1", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.LOADED, Collections.emptyList());
        DroneEntity idleDrone = new DroneEntity("serial2", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.IDLE, Collections.emptyList());
        DroneEntity loadedDrone2 = new DroneEntity("serial3", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.LOADED, Collections.emptyList());
        when(droneRepository.findAll()).thenReturn(Arrays.asList(loadedDrone1, idleDrone, loadedDrone2));

        List<DroneEntity> loadedDrones = droneService.getLoadedDrones();

        assertIterableEquals(Arrays.asList(loadedDrone1, loadedDrone2), loadedDrones);
    }
}