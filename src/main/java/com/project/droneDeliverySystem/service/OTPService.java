package com.project.droneDeliverySystem.service;

import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.OtpExpiredException;
import com.project.droneDeliverySystem.exception.OtpInvalidException;
import com.project.droneDeliverySystem.exception.ResourceNotFoundException;
import com.project.droneDeliverySystem.exception.SendOtpFailedException;
import com.project.droneDeliverySystem.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
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

    public String generateOtpCode() {return String.format("%06d", new Random().nextInt(999999));}

    public void isOtpValid(String email, String otp) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException("User Not Found");
        }
        User user = userOptional.get();
        Long otpGeneratedAt = user.getOtpGeneratedAt();

        if(System.currentTimeMillis() - otpGeneratedAt > OTP_EXPIRATION_TIME) {
            throw new OtpExpiredException("OTP has expired.");
        }
        boolean valid = Objects.equals(otp, user.getOtp());
        if(!valid) {
            throw new OtpInvalidException("OTP is invalid");
        }
        user.setConfirmedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void sendOtp(User user) {
        var otp = generateOtpCode();
        user.setOtp(otp);
        user.setOtpGeneratedAt(System.currentTimeMillis());
        userRepository.save(user);
        try{
            emailService.sendOtpEmail(user.getEmail(),otp);
        } catch (MessagingException e) {
            throw new SendOtpFailedException("OTP failed to send");
        }
    }
}
