package com.sps.parkingsystem.model;

import com.sps.parkingsystem.enums.SlotStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class ParkingSlot {
    @Id
    private String slotId;
    private String slotType;
    @Enumerated(EnumType.STRING)
    private SlotStatus status;
}
