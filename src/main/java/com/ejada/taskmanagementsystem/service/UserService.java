package com.ejada.taskmanagementsystem.service;

import com.ejada.taskmanagementsystem.model.User;
import com.ejada.taskmanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.userRepository = repo;
        this.passwordEncoder = encoder;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow();
    }

    public User register(User user) {
        validateUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    private static void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User request cannot be null");
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required and cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required and cannot be empty");
        }
        if (user.getRole() == null) {
            user.setRole(User.Role.VIEW);
        }
    }
}
