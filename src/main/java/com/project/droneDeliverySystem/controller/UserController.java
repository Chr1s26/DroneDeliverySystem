package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.dto.UserProfileDto;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/{id}")
    public UserProfileDto getProfile(@PathVariable Long id) {
        User u = userRepo.findById(id).orElseThrow();
        return new UserProfileDto(u);
    }


//    @PutMapping("/{id}")
//    public String updateProfile(@PathVariable Long id,
//                                @RequestBody UserProfileDto dto) {
//        User u = userRepo.findById(id).orElseThrow();
//        u.setName(dto.getName());
//        u.setEmail(dto.getEmail());
//        u.setPhone(dto.getPhone());
//        userRepo.save(u);
//        return "Profile updated";
//    }
}
