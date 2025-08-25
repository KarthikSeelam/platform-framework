package com.ican.cortex.platform.api.primary.impl;

import com.ican.cortex.platform.api.primary.entity.User;
import com.ican.cortex.platform.api.dto.UserDTO;
import com.ican.cortex.platform.api.mapper.UserMapper;
import com.ican.cortex.platform.api.primary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "spring.datasource.enable-primary", havingValue = "true")
public class UserService {

    private final UserRepository userRepository;


    @Autowired(required = false) // Mark injection as optional
    public UserService( UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private UserMapper userMapper;

    // Create or update a user
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Find all users
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    // Find a user by ID
    public Optional<UserDTO> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toDTO);
    }

    // Find a user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Delete a user by ID
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
