package com.sps.parkingsystem.dto.response;

import com.sps.parkingsystem.enums.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotResponse {
    private String slotId;
    private String slotType;
    private SlotStatus status;
}

