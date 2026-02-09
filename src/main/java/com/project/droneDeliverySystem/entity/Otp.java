package com.project.droneDeliverySystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Otp {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String code;
    private LocalDateTime createdAt = LocalDateTime.now();
}