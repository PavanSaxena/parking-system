package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.service.SlotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/slots")
public class SlotController {
    private final SlotService slotService;
    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }
    @GetMapping
    public List<ParkingSlot> getAllSlots() {
        return slotService.getAllSlots();
    }
    @GetMapping("/available")
    public List<ParkingSlot> getAvailableSlots() {
        return slotService.getAvailableSlots();
    }
    @PutMapping("/{slotId}/occupy")
    public ParkingSlot occupy(@PathVariable String slotId) {
        return slotService.occupySlot(slotId);
    }
}
