package com.sps.parkingsystem.dto.response;

import com.sps.parkingsystem.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String paymentId;
    private String ticketId;
    private double amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentTime;
}

