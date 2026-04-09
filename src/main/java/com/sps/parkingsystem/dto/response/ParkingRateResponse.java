package com.sps.parkingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRateResponse {
    private String rateId;
    private String vehicleType;
    private double hourlyRate;
}

