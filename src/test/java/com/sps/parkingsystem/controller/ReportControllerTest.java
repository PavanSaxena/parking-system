package com.sps.parkingsystem.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sps.parkingsystem.service.ReportService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    void occupancyEndpointReturnsExpectedMetrics() throws Exception {
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

