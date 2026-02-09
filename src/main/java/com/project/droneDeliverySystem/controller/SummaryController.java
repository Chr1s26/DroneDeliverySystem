package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class SummaryController {

    @GetMapping("/summary")
    public String summaryPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/api/auth/login";
        }
        model.addAttribute("user", user);
        return "summary";
    }

    @GetMapping("/success")
    public String successPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/api/auth/login";
        }
        return "success";
    }
}

