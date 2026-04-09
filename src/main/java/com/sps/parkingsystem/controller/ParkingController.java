package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.ParkingEntryRequest;
import com.sps.parkingsystem.dto.ParkingExitRequest;
import com.sps.parkingsystem.dto.response.ParkingTicketResponse;
import com.sps.parkingsystem.mapper.ResponseMapper;
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
    public ParkingTicketResponse enterVehicle(@Valid @RequestBody ParkingEntryRequest request){
        return ResponseMapper.toParkingTicketResponse(parkingService.enterVehicle(
                request.getVehicleNumber(),
                request.getOperatorId()
        ));
    }

    @PostMapping("/exit")
    public ParkingTicketResponse exitVehicle(@Valid @RequestBody ParkingExitRequest request){
        return ResponseMapper.toParkingTicketResponse(parkingService.exitVehicle(request.getTicketId()));
    }
}
