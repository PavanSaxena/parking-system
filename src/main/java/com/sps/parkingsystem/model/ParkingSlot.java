package com.sps.parkingsystem.model;

import com.sps.parkingsystem.enums.SlotStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlot {
    @Id
    private String slotId;
    private String slotType;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;
}