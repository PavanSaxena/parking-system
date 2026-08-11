package com.sps.parkingsystem.service;

import com.sps.parkingsystem.dto.LoginRequestDTO;
import com.sps.parkingsystem.dto.LoginResponseDTO;
import com.sps.parkingsystem.exception.InvalidCredentialsException;
import com.sps.parkingsystem.model.ParkingOperator;
import com.sps.parkingsystem.repository.ParkingOperatorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private ParkingOperatorRepository operatorRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void loginReturnsTokenAndOperatorDetailsForValidCredentials() {
        ParkingOperator operator = new ParkingOperator();
        operator.setUserId("OP-1");
        operator.setUserName("Alice");
        operator.setEmail("alice@example.com");
        operator.setPassword("secret");

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("alice@example.com");
        request.setPassword("secret");

        when(operatorRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(operator));

        LoginResponseDTO response = authService.login(request);

        assertEquals("OP-1", response.getUserId());
        assertEquals("Alice", response.getUserName());
        assertEquals("alice@example.com", response.getEmail());
        assertNotNull(response.getToken());
        assertFalse(response.getToken().isBlank());
    }

    @Test
    void loginThrowsWhenPasswordIsIncorrect() {
        ParkingOperator operator = new ParkingOperator();
        operator.setPassword("correct");

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("alice@example.com");
        request.setPassword("wrong");

        when(operatorRepository.findByEmail("alice@example.com")).thenReturn(Optional.of(operator));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
    }
}
