package com.sps.parkingsystem.dto;

import jakarta.validation.constraints.NotBlank;

public class PaymentRequest {
    @NotBlank
    private String paymentId;
    public String getPaymentId() {return paymentId;}
    public void setPaymentId(String paymentId) {this.paymentId = paymentId;}
}
