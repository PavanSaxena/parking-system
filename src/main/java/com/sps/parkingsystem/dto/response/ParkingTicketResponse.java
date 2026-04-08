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
public class ParkingTicketResponse {
    private String ticketId;
    private String vehicleNumber;
    private String slotId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private PaymentStatus paymentStatus;
}

