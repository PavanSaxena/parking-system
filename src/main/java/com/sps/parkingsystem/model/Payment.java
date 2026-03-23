package com.sps.parkingsystem.model;

import com.sps.parkingsystem.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Payment {
    @Id
    private String paymentId;
    private double amount;
    private LocalDateTime paymentTime;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @OneToOne
    @JoinColumn(name = "ticket_id")
    private ParkingTicket ticket;
}
