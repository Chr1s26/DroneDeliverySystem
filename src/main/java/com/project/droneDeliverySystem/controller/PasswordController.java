package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.entity.Otp;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.repository.OtpRepository;
import com.project.droneDeliverySystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private OtpRepository otpRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/forgot")
    public String forgot(@RequestParam String email) {
        String code = String.valueOf(100000 + new Random().nextInt(900000));

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(code);
        otpRepo.save(otp);

        System.out.println("OTP for " + email + ": " + code);

        return "OTP sent";
    }

    @PostMapping("/reset")
    public String reset(@RequestParam String email,
                        @RequestParam String code,
                        @RequestParam String newPassword) {

        Otp otp = otpRepo.findByEmailAndCode(email, code)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(encoder.encode(newPassword));
        userRepo.save(user);

        return "Password reset successful";
    }
}

