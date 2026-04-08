package com.sps.parkingsystem.exception;

public class DuplicateVehicleEntryException extends RuntimeException {
    public DuplicateVehicleEntryException(String message) {
        super(message);
    }
}

