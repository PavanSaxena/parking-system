package com.sps.parkingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
public class ParkingRate {
    @Id
    private String rateId;
    private String vehicleType;
    private double hourlyRate;
    @ManyToOne
    private Administrator administrator;
}
