package com.project.droneDeliverySystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DeliveryDto {

    @NotBlank(message = "Package name is required")
    private String packageName;

    @NotBlank(message = "Package description is required")
    private String packageDescription;

    private String senderName;
    @NotBlank(message = "Receiver name is required")
    @Pattern(
            regexp = "^[A-Za-z ]{2,30}$",
            message = "Please enter a valid receiver name"
    )
    private String receiverName;

    @NotBlank(message = "Receiver email is required")
    @Email(message = "Invalid email format")
    private String receiverEmail;

    @NotBlank(message = "Receiver phone is required")
    @Pattern(
            regexp = "^[0-9]{7,15}$",
            message = "Phone must be 7–15 digits"
    )
    private String receiverPhone;

    private double sourceLat;
    private double sourceLng;
    private double destLat;
    private double destLng;

    private Long userId;
}


