package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.CreateSlotRequest;
import com.sps.parkingsystem.exception.GlobalExceptionHandler;
import com.sps.parkingsystem.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc buildMockMvc() {
        return MockMvcBuilders.standaloneSetup(adminController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    void addSlotReturnsCreatedSlotResponse() throws Exception {
        MockMvc mockMvc = buildMockMvc();

        when(adminService.addParkingSlot(any(), any(), any())).thenReturn(new com.sps.parkingsystem.model.ParkingSlot("S10", "CAR", com.sps.parkingsystem.enums.SlotStatus.AVAILABLE));

        mockMvc.perform(post("/admin/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slotId\":\"S10\",\"slotType\":\"CAR\",\"status\":\"AVAILABLE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slotId").value("S10"));
    }

    @Test
    void removeSlotReturnsOk() throws Exception {
        MockMvc mockMvc = buildMockMvc();

        mockMvc.perform(delete("/admin/slots/S10"))
                .andExpect(status().isOk());
    }
}
