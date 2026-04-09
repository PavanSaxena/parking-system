package com.sps.parkingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    private String vehicleNumber;
    private String vehicleType;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private VehicleOwner owner;
    @JsonIgnore
    @OneToMany(mappedBy = "vehicle")
    private List<ParkingTicket> tickets;
}