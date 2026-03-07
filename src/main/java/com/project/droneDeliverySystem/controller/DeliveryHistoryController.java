package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.ResourceNotFoundException;
import com.project.droneDeliverySystem.repository.DeliveryRepository;
import com.project.droneDeliverySystem.repository.UserRepository;
import com.project.droneDeliverySystem.service.DeliveryService;
import com.project.droneDeliverySystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryHistoryController {

    private final UserService userService;
    private final DeliveryService deliveryService;

    @GetMapping("/history")
    public String viewHistory(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/api/auth/login";
        }

        User user = userService.findById(userId);
        List<Delivery> deliveries = deliveryService.findByUserId(user.getId());
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("user", user);

        return "history";
    }
}
