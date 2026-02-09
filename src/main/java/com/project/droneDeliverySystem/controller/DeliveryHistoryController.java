package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.repository.DeliveryRepository;
import com.project.droneDeliverySystem.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DeliveryHistoryController {

    @Autowired
    private DeliveryRepository deliveryRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/history")
    public String viewHistory(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/login";
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Delivery> deliveries = deliveryRepo.findByUser(user);

        model.addAttribute("deliveries", deliveries);
        model.addAttribute("user", user);

        return "history";
    }
}
