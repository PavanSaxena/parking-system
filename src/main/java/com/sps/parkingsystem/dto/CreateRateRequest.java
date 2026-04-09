package com.sps.parkingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRateRequest {
    @NotBlank
    private String rateId;
    @NotBlank
    private String vehicleType;
    @Positive
    private double hourlyRate;

}
