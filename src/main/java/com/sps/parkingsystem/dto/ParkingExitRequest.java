package com.sps.parkingsystem.dto;

import jakarta.validation.constraints.NotBlank;

public class ParkingExitRequest {
    @NotBlank
    private String ticketId;
    public String getTicketId() {return ticketId;}
    public void setTicketId(String ticketId) {this.ticketId = ticketId;}
}
