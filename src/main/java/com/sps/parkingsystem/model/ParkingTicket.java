package com.sps.parkingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class ParkingTicket {
    @Id
    private String ticketId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    @ManyToOne
    private Vehicle vehicle;
    @ManyToOne
    private ParkingSlot slot;
    @ManyToOne
    private ParkingOperator operator;
    @OneToOne(mappedBy = "ticket")
    private Payment payment;
}
