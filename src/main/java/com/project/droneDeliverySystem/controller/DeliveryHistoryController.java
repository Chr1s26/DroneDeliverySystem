package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.ResourceNotFoundException;
import com.project.droneDeliverySystem.repository.DeliveryRepository;
import com.project.droneDeliverySystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
public class DeliveryHistoryController {

    @Autowired
    private DeliveryRepository deliveryRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/history")
    public String viewHistory(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/api/auth/login";
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found when viewing history"));

        List<Delivery> deliveries = deliveryRepo.findByUser(user);

        model.addAttribute("deliveries", deliveries);
        model.addAttribute("user", user);

        return "history";
    }
}
