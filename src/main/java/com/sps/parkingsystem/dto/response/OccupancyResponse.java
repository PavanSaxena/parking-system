package com.sps.parkingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OccupancyResponse {
    private long totalSlots;
    private long occupiedSlots;
    private double occupancyPercentage;
}

