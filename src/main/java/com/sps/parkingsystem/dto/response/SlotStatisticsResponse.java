package com.sps.parkingsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SlotStatisticsResponse {
    private long totalSlots;
    private long availableSlots;
    private long occupiedSlots;
}

