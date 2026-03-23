package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.ParkingEntryRequest;
import com.sps.parkingsystem.model.ParkingTicket;
import com.sps.parkingsystem.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking")
public class ParkingController {
    private final ParkingService parkingService;
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }
    @PostMapping("/entry")
    public ParkingTicket enterVehicle(@Valid @RequestBody ParkingEntryRequest request){
        return parkingService.enterVehicle(
                request.getVehicleNumber(),
                request.getSlotId(),
                request.getOperatorId()
        );
    }
    @PostMapping("/exit/{ticketId}")
    public ParkingTicket exitVehicle(@PathVariable String ticketId){
        return parkingService.exitVehicle(ticketId);
    }
}
