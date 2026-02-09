package com.project.droneDeliverySystem.service;

import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.ResourceNotFoundException;
import com.project.droneDeliverySystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    public String register(User user, String confirmPassword) {

        if (!user.getPassword().equals(confirmPassword)) {
            return "Password does not match";
        }

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return "Email already exists";
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");

        userRepo.save(user);
        return "SUCCESS";
    }

    public void resetPassword(String email, String password) {
        Optional<User> userOp = userRepo.findByEmail(email);
        User user = userOp.get();
        if (userOp.isPresent()) {
            user.setPassword(encoder.encode(password));
            userRepo.save(user);
        }else{
            throw new ResourceNotFoundException("User Not Found");
        }
    }
}
