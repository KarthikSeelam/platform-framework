package com.ican.cortex.platform.api.telemetry.impl;

import com.ican.cortex.platform.api.mapper.UserMapper;

import com.ican.cortex.platform.api.telemetry.entity.User;
import com.ican.cortex.platform.api.telemetry.repository.TelemetryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "spring.datasource.enable-telemetry", havingValue = "true")
public class TelemetryUserService {

    private final TelemetryUserRepository telemetryUserRepository;


    @Autowired(required = false) // Mark injection as optional
    public TelemetryUserService(TelemetryUserRepository telemetryUserRepository) {
        this.telemetryUserRepository = telemetryUserRepository;
    }

    @Autowired
    private UserMapper userMapper;

    // Create or update a user
    @Transactional
    public User saveUser(User user) {
        return telemetryUserRepository.save(user);
    }

    // Find a user by email
    public User getUserByEmail(String email) {
        return telemetryUserRepository.findByEmail(email);
    }

    // Delete a user by ID
    @Transactional
    public void deleteUserById(Long id) {
        telemetryUserRepository.deleteById(id);
    }
}
