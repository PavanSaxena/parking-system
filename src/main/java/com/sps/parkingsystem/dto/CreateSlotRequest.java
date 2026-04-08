package com.sps.parkingsystem.dto;

import com.sps.parkingsystem.enums.SlotStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSlotRequest {
    @NotBlank
    private String slotId;
    @NotBlank
    private String slotType;
    private SlotStatus status;
}

