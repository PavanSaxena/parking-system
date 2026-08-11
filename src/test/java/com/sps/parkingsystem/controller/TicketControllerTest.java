package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.TicketCreateRequest;
import com.sps.parkingsystem.exception.GlobalExceptionHandler;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.service.TicketService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @Test
    void createTicketReturnsTicketResponse() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ticketController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(new LocalValidatorFactoryBean())
                .build();

        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        when(ticketService.createTicket(any(), any(), any(), any())).thenReturn(ticket);

        mockMvc.perform(post("/tickets/entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ticketId\":\"T1\",\"vehicleNumber\":\"KA01\",\"slotId\":\"S1\",\"operatorId\":\"OP-1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value("T1"));
    }

    @Test
    void closeTicketReturnsTicketResponse() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");
        when(ticketService.closeTicket("T1")).thenReturn(ticket);

        mockMvc.perform(put("/tickets/T1/exit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketId").value("T1"));
    }
}
