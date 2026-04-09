package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.enums.PaymentStatus;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.service.PaymentService;
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
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void processPaymentReturnsResponseDto() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        ParkingTicket ticket = new ParkingTicket();
        ticket.setTicketId("T1");

        Payment payment = new Payment();
        payment.setPaymentId("P1");
        payment.setTicket(ticket);
        payment.setAmount(150.0);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentTime(LocalDateTime.now());

        when(paymentService.processPayment(eq("T1"))).thenReturn(payment);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ticketId\":\"T1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value("P1"))
                .andExpect(jsonPath("$.ticketId").value("T1"))
                .andExpect(jsonPath("$.paymentStatus").value("COMPLETED"));
    }
}





