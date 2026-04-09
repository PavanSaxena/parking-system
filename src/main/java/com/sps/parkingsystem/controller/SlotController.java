package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.response.SlotResponse;
import com.sps.parkingsystem.dto.response.SlotStatisticsResponse;
import com.sps.parkingsystem.mapper.ResponseMapper;
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
    public List<SlotResponse> getAllSlots() {
        return slotService.getAllSlots().stream().map(ResponseMapper::toSlotResponse).toList();
    }

    @GetMapping("/available")
    public List<SlotResponse> getAvailableSlots() {
        return slotService.getAvailableSlots().stream().map(ResponseMapper::toSlotResponse).toList();
    }

    @GetMapping("/occupied")
    public List<SlotResponse> getOccupiedSlots() {
        return slotService.getOccupiedSlots().stream().map(ResponseMapper::toSlotResponse).toList();
    }

    @GetMapping("/type/{slotType}")
    public List<SlotResponse> getByType(@PathVariable String slotType) {
        return slotService.getSlotsByType(slotType).stream().map(ResponseMapper::toSlotResponse).toList();
    }

    @PutMapping("/{slotId}/occupy")
    public SlotResponse occupy(@PathVariable String slotId) {
        return ResponseMapper.toSlotResponse(slotService.occupySlot(slotId));
    }

    @PutMapping("/{slotId}/free")
    public SlotResponse free(@PathVariable String slotId) {
        return ResponseMapper.toSlotResponse(slotService.freeSlot(slotId));
    }

    @GetMapping("/stats")
    public SlotStatisticsResponse getStatistics() {
        return slotService.getStatistics();
    }
}
