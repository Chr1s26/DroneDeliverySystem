package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.dto.DeliveryDto;
import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.repository.DeliveryRepository;
import com.project.droneDeliverySystem.service.DeliveryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/send")
    public String sendPackagePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/api/auth/login";
        }
        model.addAttribute("user", user);
        return "send-package";
    }


    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> create(@Valid @RequestBody DeliveryDto dto, BindingResult bindingResult, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("NOT_AUTHENTICATED");
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        deliveryService.createDelivery(dto,user);

        return ResponseEntity.ok("SUCCESS");
    }


    @GetMapping("/history")
    @ResponseBody
    public List<Delivery> history(HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return List.of();
        }
        return deliveryService.findByUserId(user.getId());
    }
}
