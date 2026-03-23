package com.sps.parkingsystem.dto;

import jakarta.validation.constraints.NotBlank;

public class ParkingEntryRequest {
    @NotBlank
    private String vehicleNumber;
    @NotBlank
    private String slotId;
    @NotBlank
    private String operatorId;
}
