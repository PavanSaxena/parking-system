package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.enums.SlotStatus;
import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.service.SlotService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SlotControllerTest {

    @Mock
    private SlotService slotService;

    @InjectMocks
    private SlotController slotController;

    @Test
    void getAvailableSlotsReturnsJsonList() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(slotController).build();
        ParkingSlot slot = new ParkingSlot("S1", "CAR", SlotStatus.AVAILABLE);
        when(slotService.getAvailableSlots()).thenReturn(List.of(slot));

        mockMvc.perform(get("/slots/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slotId").value("S1"));
    }

    @Test
    void occupyAndFreeEndpointsReturnSlotResponses() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(slotController).build();
        ParkingSlot occupied = new ParkingSlot("S1", "CAR", SlotStatus.OCCUPIED);
        ParkingSlot freed = new ParkingSlot("S1", "CAR", SlotStatus.AVAILABLE);

        when(slotService.occupySlot("S1")).thenReturn(occupied);
        when(slotService.freeSlot("S1")).thenReturn(freed);

        mockMvc.perform(put("/slots/S1/occupy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OCCUPIED"));

        mockMvc.perform(put("/slots/S1/free"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }
}
