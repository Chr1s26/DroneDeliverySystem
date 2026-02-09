package com.project.droneDeliverySystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role = "ROLE_USER";
    private String otp;
    private Long otpGeneratedAt;
    private LocalDateTime confirmedAt;
}

