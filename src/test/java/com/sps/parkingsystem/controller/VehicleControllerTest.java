package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.exception.GlobalExceptionHandler;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    @Test
    void createVehicleReturnsVehicleResponse() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(vehicleController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA01");
        vehicle.setVehicleType("CAR");
        when(vehicleService.createVehicle(any(), any(), any())).thenReturn(vehicle);

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"vehicleNumber\":\"KA01\",\"vehicleType\":\"CAR\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleNumber").value("KA01"));
    }

    @Test
    void getAllVehiclesReturnsList() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA02");
        vehicle.setVehicleType("BIKE");
        when(vehicleService.getAllVehicles()).thenReturn(List.of(vehicle));

        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].vehicleNumber").value("KA02"));
    }
}
