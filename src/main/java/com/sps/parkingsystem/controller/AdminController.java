package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.dto.CreateSlotRequest;
import com.sps.parkingsystem.dto.response.SlotResponse;
import com.sps.parkingsystem.mapper.ResponseMapper;
import com.sps.parkingsystem.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/slots")
    public SlotResponse addSlot(@Valid @RequestBody CreateSlotRequest request){
        return ResponseMapper.toSlotResponse(adminService.addParkingSlot(
                request.getSlotId(),
                request.getSlotType(),
                request.getStatus()
        ));
    }

    @DeleteMapping("/slots/{id}")
    public void removeSlot(@PathVariable String id){
        adminService.removeParkingSlot(id);
    }
}