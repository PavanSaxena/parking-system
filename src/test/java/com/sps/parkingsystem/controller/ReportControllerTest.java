package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @Test
    void reportEndpointsReturnListsAndRevenue() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();

        when(reportService.getAllTickets()).thenReturn(List.of(new ParkingTicket()));
        when(reportService.getActiveTickets()).thenReturn(List.of(new ParkingTicket()));
        when(reportService.getAllPayments()).thenReturn(List.of(new Payment()));
        when(reportService.getTotalRevenue()).thenReturn(125.0);

        mockMvc.perform(get("/reports/tickets"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/reports/active"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/reports/payments"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/reports/revenue"))
                .andExpect(status().isOk())
                .andExpect(content().string("125.0"));
    }
}


