package com.sps.parkingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingEntryRequest {
    @NotBlank
    private String vehicleNumber;
    @NotBlank
    private String operatorId;
}
