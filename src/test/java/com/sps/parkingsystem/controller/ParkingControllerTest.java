package com.sps.parkingsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingController.class)
class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParkingService parkingService;

    @Test
    void entryReturnsTicketResponseDto() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA01AB1234");

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId("S1");

        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        ticket.setVehicle(vehicle);
        ticket.setSlot(slot);
        ticket.setEntryTime(LocalDateTime.now());

        when(parkingService.enterVehicle(eq("KA01AB1234"), eq("OP-1"))).thenReturn(ticket);

        mockMvc.perform(post("/parking/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EntryPayload("KA01AB1234", "OP-1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value("T1"))
                .andExpect(jsonPath("$.vehicleNumber").value("KA01AB1234"))
                .andExpect(jsonPath("$.slotId").value("S1"));
    }

    private record EntryPayload(String vehicleNumber, String operatorId) {
    }
}

