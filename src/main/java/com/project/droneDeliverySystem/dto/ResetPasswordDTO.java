package com.project.droneDeliverySystem.dto;

import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String password;
    private String confirmPassword;
}
