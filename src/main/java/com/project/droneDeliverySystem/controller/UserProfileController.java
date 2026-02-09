package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.dto.UserProfileDto;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.ResourceNotFoundException;
import com.project.droneDeliverySystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/edit")
    public String editProfile(Model model,HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/api/auth/login";
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found when editing profile"));


        model.addAttribute("profile", new UserProfileDto(user));
        model.addAttribute("user", user);

        return "profile/edit-profile";
    }

    @PostMapping("/edit")
    public String updateProfile(@Valid @ModelAttribute("profile") UserProfileDto dto, BindingResult bindingResult, Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/api/auth/login";
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found when editing profile"));


        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "profile/edit-profile";
        }

        if (userRepo.existsByEmailAndIdNot(dto.getEmail(), userId)) {
            bindingResult.rejectValue("email", "duplicate", "Email already in use");
            model.addAttribute("user", user);
            return "profile/edit-profile";
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        userRepo.save(user);

        model.addAttribute("success", "Profile updated successfully");
        model.addAttribute("profile", dto);
        model.addAttribute("user", user);

        return "profile/edit-profile";
    }
}
