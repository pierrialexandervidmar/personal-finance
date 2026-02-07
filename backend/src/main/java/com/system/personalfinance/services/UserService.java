package com.system.personalfinance.services;

import com.system.personalfinance.dtos.user.CreateUserRequest;
import com.system.personalfinance.entities.User;
import com.system.personalfinance.exceptions.ConflictException;
import com.system.personalfinance.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(CreateUserRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new ConflictException("Email já existe né bixo");
        }

        String hash = passwordEncoder.encode(req.getPassword());
        User user = new User(req.getName(), req.getEmail(), hash);

        return userRepository.save(user);
    }
}
