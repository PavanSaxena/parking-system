package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.exception.GlobalExceptionHandler;
import com.sps.parkingsystem.model.ParkingRate;
import com.sps.parkingsystem.service.ParkingRateService;
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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ParkingRateControllerTest {

    @Mock
    private ParkingRateService rateService;

    @InjectMocks
    private ParkingRateController parkingRateController;

    @Test
    void createRateReturnsRateResponse() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(parkingRateController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();

        ParkingRate rate = new ParkingRate();
        rate.setRateId("R1");
        when(rateService.createRate(any(), any(), anyDouble())).thenReturn(rate);

        mockMvc.perform(post("/rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rateId\":\"R1\",\"vehicleType\":\"CAR\",\"hourlyRate\":20.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateId").value("R1"));
    }

    @Test
    void getAllRatesReturnsList() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(parkingRateController).build();
        ParkingRate rate = new ParkingRate();
        rate.setRateId("R2");
        when(rateService.getAllRates()).thenReturn(List.of(rate));

        mockMvc.perform(get("/rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rateId").value("R2"));
    }
}
