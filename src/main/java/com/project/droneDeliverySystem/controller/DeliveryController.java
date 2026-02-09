package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.dto.DeliveryDto;
import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.repository.DeliveryRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
public class DeliveryController {

    @Autowired
    private DeliveryRepository repo;

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

        Delivery d = new Delivery();
        d.setPackageName(dto.getPackageName());
        d.setPackageDescription(dto.getPackageDescription());

        d.setSenderName(user.getName());
        d.setReceiverName(dto.getReceiverName());
        d.setReceiverEmail(dto.getReceiverEmail());
        d.setReceiverPhone(dto.getReceiverPhone());

        d.setSourceLat(dto.getSourceLat());
        d.setSourceLng(dto.getSourceLng());
        d.setDestLat(dto.getDestLat());
        d.setDestLng(dto.getDestLng());

        d.setStatus("PENDING");
        d.setUser(user);

        repo.save(d);

        return ResponseEntity.ok("SUCCESS");
    }


    @GetMapping("/history")
    @ResponseBody
    public List<Delivery> history(HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return List.of();
        }

        return repo.findByUser_Id(user.getId());
    }
}
