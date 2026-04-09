package com.sps.parkingsystem.config;

import com.sps.parkingsystem.model.ParkingOperator;
import com.sps.parkingsystem.repository.ParkingOperatorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OperatorDataInitializer {

    @Bean
    CommandLineRunner seedOperator(ParkingOperatorRepository operatorRepository) {
        return args -> {
            String seededEmail = "operator1@parking.local";
            if (operatorRepository.findByEmail(seededEmail).isEmpty()) {
                ParkingOperator operator = new ParkingOperator();
                operator.setUserId("OP-1");
                operator.setUserName("Operator One");
                operator.setEmail(seededEmail);
                operator.setPassword("op123");
                operatorRepository.save(operator);
            }
        };
    }
}

