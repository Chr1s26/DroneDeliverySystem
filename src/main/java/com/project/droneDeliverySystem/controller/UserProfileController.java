package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.dto.UserProfileDto;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/edit")
    public String editProfile(Model model,HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/login";
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        model.addAttribute("profile", new UserProfileDto(user));
        model.addAttribute("user", user);

        return "profile/edit-profile";
    }

    @PostMapping("/edit")
    public String updateProfile(
            @Valid @ModelAttribute("profile") UserProfileDto dto,
            BindingResult bindingResult,
            Model model,
            HttpSession session

    ) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/login";
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (bindingResult.hasErrors()) {
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
