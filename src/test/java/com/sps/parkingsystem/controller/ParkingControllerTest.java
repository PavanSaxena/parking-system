package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Vehicle;
import com.sps.parkingsystem.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ParkingControllerTest {

    @Mock
    private ParkingService parkingService;

    @InjectMocks
    private ParkingController parkingController;

    @Test
    void entryReturnsTicketResponseDto() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(parkingController).build();

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
                        .content("{\"vehicleNumber\":\"KA01AB1234\",\"operatorId\":\"OP-1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value("T1"))
                .andExpect(jsonPath("$.vehicleNumber").value("KA01AB1234"))
                .andExpect(jsonPath("$.slotId").value("S1"));
    }

    @Test
    void exitReturnsTicketResponseDto() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(parkingController).build();

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber("KA01AB1234");

        ParkingSlot slot = new ParkingSlot();
        slot.setSlotId("S1");

        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        ticket.setVehicle(vehicle);
        ticket.setSlot(slot);
        ticket.setExitTime(LocalDateTime.now());

        when(parkingService.exitVehicle(eq("T1"))).thenReturn(ticket);

        mockMvc.perform(post("/parking/exit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ticketId\":\"T1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value("T1"))
                .andExpect(jsonPath("$.slotId").value("S1"));
    }
}



