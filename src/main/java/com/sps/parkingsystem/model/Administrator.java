package com.sps.parkingsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
public class Administrator extends User {
    @OneToMany(mappedBy = "administrator")
    private List<ParkingRate> parkingRates;
}
