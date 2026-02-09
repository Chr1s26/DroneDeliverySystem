package com.project.droneDeliverySystem.service;

import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.OtpExpiredException;
import com.project.droneDeliverySystem.exception.OtpInvalidException;
import com.project.droneDeliverySystem.exception.ResourceNotFoundException;
import com.project.droneDeliverySystem.exception.SendOtpFailedException;
import com.project.droneDeliverySystem.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPService {

    private final UserRepository userRepository;
    private static final long OTP_EXPIRATION_TIME = 1000 * 5 * 60;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public String generateOtpCode() {return String.format("%06d", new Random().nextInt(999999));}

    public void isOtpValid(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getOtpExpiresAt() == null ||
                user.getOtpExpiresAt().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP has expired");
        }

        if (!passwordEncoder.matches(otp, user.getOtp())) {
            throw new OtpInvalidException("Invalid OTP");
        }

        user.setOtp(null);
        user.setOtpExpiresAt(null);
        user.setConfirmedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    public void sendOtp(User user) {
        var otp = generateOtpCode();
        user.setOtp(otp);
        user.setOtpExpiresAt(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);
        try{
            emailService.sendOtpEmail(user.getEmail(),otp);
        } catch (MessagingException e) {
            throw new SendOtpFailedException("OTP failed to send");
        }
    }
}
