package com.sps.parkingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sps.parkingsystem.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private String paymentId;
    private double amount;
    private LocalDateTime paymentTime;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "ticket_id")
    private ParkingTicket ticket;
}