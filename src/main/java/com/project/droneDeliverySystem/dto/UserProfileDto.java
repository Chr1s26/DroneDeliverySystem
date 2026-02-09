package com.project.droneDeliverySystem.dto;

import com.project.droneDeliverySystem.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserProfileDto {

    @Pattern(
            regexp = "^[A-Za-z ]{2,30}$",
            message = "Please enter a valid user name"
    )
    private String name;
    @Email(message = "Invalid email format")
    private String email;
    @Pattern(
            regexp = "^[0-9]{7,15}$",
            message = "Phone number must be 7–15 digits"
    )
    private String phone;

    public UserProfileDto() {}

    public UserProfileDto(User u) {
        this.name = u.getName();
        this.email = u.getEmail();
        this.phone = u.getPhone();
    }
}

