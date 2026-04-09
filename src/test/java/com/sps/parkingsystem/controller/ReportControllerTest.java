package com.sps.parkingsystem.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sps.parkingsystem.service.ReportService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportController reportController;

    @Test
    void occupancyEndpointReturnsExpectedMetrics() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();

        when(reportService.getTotalSlots()).thenReturn(10L);
        when(reportService.getOccupiedSlots()).thenReturn(4L);
        when(reportService.getOccupancyPercentage()).thenReturn(40.0);

        mockMvc.perform(get("/reports/occupancy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSlots").value(10))
                .andExpect(jsonPath("$.occupiedSlots").value(4))
                .andExpect(jsonPath("$.occupancyPercentage").value(40.0));
    }
}


