package com.task.musala.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.musala.entity.DroneEntity;
import com.task.musala.entity.DroneModel;
import com.task.musala.entity.DroneState;
import com.task.musala.entity.MedicationEntity;
import com.task.musala.exceptions.DroneStateException;
import com.task.musala.exceptions.NotFoundException;
import com.task.musala.model.Medication;
import com.task.musala.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class DroneControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DroneService droneService;

    @InjectMocks
    private DroneController droneController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(droneController).build();
    }

    @Test
    public void testRegisterDrone() throws Exception {
        doNothing().when(droneService).registerDrone(any(DroneEntity.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/drone-service/register-drone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serialNumber\":\"DRONE001\",\"model\":\"LIGHTWEIGHT\",\"weightLimit\":500.0,\"batteryCapacity\":100,\"state\":\"IDLE\"}"))
                .andExpect(status().isCreated());

        doThrow(new IllegalArgumentException("Weight limit must be 500 or less")).when(droneService).registerDrone(any(DroneEntity.class));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/drone-service/register-drone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serialNumber\":\"DRONE001\",\"model\":\"LIGHTWEIGHT\",\"weightLimit\":1000.0,\"batteryCapacity\":100,\"state\":\"IDLE\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = "Weight limit must be 500 or less";
        assertEquals(expectedResponse, actualResponse);

        doThrow(new DuplicateKeyException("Drone with that serial number already exists")).when(droneService).registerDrone(any(DroneEntity.class));

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/drone-service/register-drone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"serialNumber\":\"DRONE001\",\"model\":\"LIGHTWEIGHT\",\"weightLimit\":500.0,\"batteryCapacity\":100,\"state\":\"IDLE\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        actualResponse = mvcResult.getResponse().getContentAsString();
        expectedResponse = "Drone with that serial number already exists";
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testLoadDroneSuccess() throws Exception {
        String droneSerialNumber = "DRONE001";
        MedicationEntity medication1 = new MedicationEntity("Medication 1", 2.5, "CODE1", new byte[]{});
        MedicationEntity medication2 = new MedicationEntity("Medication 2", 3.2, "CODE2", new byte[]{});
        List<MedicationEntity> medications = Arrays.asList(medication1, medication2);

        doNothing().when(droneService).loadDrone(droneSerialNumber, medications);

        mockMvc.perform(MockMvcRequestBuilders.post("/drone-service/load-drone")
                        .param("serialNumber", droneSerialNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\":\"Medication 1\",\"weight\":2.5,\"code\":\"CODE1\",\"image\":\"\"},{\"name\":\"Medication 2\",\"weight\":3.2,\"code\":\"CODE2\",\"image\":\"\"}]"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoadDroneNotFound() throws Exception {
        String droneSerialNumber = "DRONE001";
        List<MedicationEntity> medications = Collections.emptyList();

        doThrow(new NotFoundException("Drone not found")).when(droneService).loadDrone(droneSerialNumber, medications);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/drone-service/load-drone")
                        .param("serialNumber", droneSerialNumber)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isNotFound())
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = "Drone not found";
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetLoadedMedications() throws Exception {
        String serialNumber = "DRONE001";
        List<MedicationEntity> medications = Arrays.asList(new MedicationEntity(), new MedicationEntity());
        when(droneService.getLoadedMedications(serialNumber)).thenReturn(medications);

        MvcResult mvcResult = mockMvc.perform(get("/drone-service/loaded-medications")
                        .param("serialNumber", serialNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        List<Medication> expectedResponseBody = medications.stream()
                .map(Medication::fromDto)
                .collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedResponseBodyJson = objectMapper.writeValueAsString(expectedResponseBody);

        assertEquals(expectedResponseBodyJson, actualResponseBody);
    }

    @Test
    public void testGetAvailableDronesForLoadingMedications() throws Exception {
        when(droneService.getAvailableDronesForLoading()).thenReturn(Collections.singletonList(new DroneEntity(
                "DRONE001", DroneModel.LIGHTWEIGHT, 500, 100, DroneState.IDLE
        )));

        mockMvc.perform(MockMvcRequestBuilders.get("/drone-service/drones")
                        .param("loadedMedications", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].serialNumber").value("DRONE001"))
                .andExpect(jsonPath("$[0].model").value("LIGHTWEIGHT"))
                .andExpect(jsonPath("$[0].batteryCapacity").value(100));
    }

    @Test
    public void testGetDroneBatteryLevel() throws Exception {
        when(droneService.getDroneBatteryLevel("DRONE001")).thenReturn(50);

        mockMvc.perform(get("/drone-service/drone-battery-level")
                        .param("serialNumber", "DRONE001"))
                .andExpect(status().isOk())
                .andExpect(content().string("50%"));

        when(droneService.getDroneBatteryLevel("DRONE002")).thenThrow(new NotFoundException("Drone not found"));

        MvcResult mvcResult = mockMvc.perform(get("/drone-service/drone-battery-level")
                        .param("serialNumber", "DRONE002"))
                .andExpect(status().isNotFound())
                .andReturn();

        String actualResponse = mvcResult.getResponse().getContentAsString();
        String expectedResponse = "Drone not found";
        assertEquals(expectedResponse, actualResponse);

        when(droneService.getDroneBatteryLevel("DRONE003")).thenThrow(new DroneStateException("Drone is not loaded"));

        mvcResult = mockMvc.perform(get("/drone-service/drone-battery-level")
                        .param("serialNumber", "DRONE003"))
                .andExpect(status().isBadRequest())
                .andReturn();

        actualResponse = mvcResult.getResponse().getContentAsString();
        expectedResponse = "Drone is not loaded";
        assertEquals(expectedResponse, actualResponse);
    }
}
