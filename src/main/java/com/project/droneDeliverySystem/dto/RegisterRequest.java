package com.project.droneDeliverySystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Pattern(
            regexp = "^[A-Za-z ]{2,30}$",
            message = "Please enter a valid user name"
    )
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter email format correctly")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^[0-9]{8,15}$",
            message = "Please enter a valid phone number"
    )
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain letters and numbers"
    )
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

