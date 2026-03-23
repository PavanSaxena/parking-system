package com.sps.parkingsystem.service;

import com.sps.parkingsystem.enums.PaymentStatus;
import com.sps.parkingsystem.model.Payment;
import com.sps.parkingsystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    public Payment processPayment(Payment payment){
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentTime(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
}
