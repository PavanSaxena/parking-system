package com.sps.parkingsystem.service;

import com.sps.parkingsystem.dto.LoginRequestDTO;
import com.sps.parkingsystem.dto.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
}

