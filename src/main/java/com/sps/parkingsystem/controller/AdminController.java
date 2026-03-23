package com.sps.parkingsystem.controller;

import com.sps.parkingsystem.model.ParkingSlot;
import com.sps.parkingsystem.service.AdminService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/slots")
    public ParkingSlot addSlot(@RequestBody ParkingSlot slot){
        return adminService.addParkingSlot(slot);
    }
    @DeleteMapping("/slots/{id}")
    public void removeSlot(@PathVariable String id){
        adminService.removeParkingSlot(id);
    }
}
