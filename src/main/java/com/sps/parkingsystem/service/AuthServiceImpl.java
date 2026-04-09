package com.sps.parkingsystem.service;

import com.sps.parkingsystem.dto.LoginRequestDTO;
import com.sps.parkingsystem.dto.LoginResponseDTO;
import com.sps.parkingsystem.exception.InvalidCredentialsException;
import com.sps.parkingsystem.model.ParkingOperator;
import com.sps.parkingsystem.repository.ParkingOperatorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final ParkingOperatorRepository operatorRepository;

    public AuthServiceImpl(ParkingOperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        ParkingOperator operator = operatorRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!operator.getPassword().equals(request.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // Stateless token for demo purposes; can be replaced with JWT without changing controller contract.
        String token = UUID.randomUUID().toString();
        return new LoginResponseDTO(token, operator.getUserId(), operator.getUserName(), operator.getEmail());
    }
}

