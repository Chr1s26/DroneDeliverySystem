package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.repository.DeliveryRepository;
import com.project.droneDeliverySystem.repository.UserRepository;
import com.project.droneDeliverySystem.service.WaypointService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DeliveryRepository repo;
    @Autowired
    private WaypointService waypointService;
    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public String adminPage(@RequestParam(defaultValue = "PENDING") String status, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ROLE_ADMIN".equals(user.getRole())) {
            return "redirect:/auth/login";
        }

        List<Delivery> deliveries = repo.findByStatusOrderByIdDesc(status);
        model.addAttribute("user", user);
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("currentStatus", status);
        model.addAttribute("pendingCount", repo.countByStatus("PENDING"));
        return "admin/deliveries";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ROLE_ADMIN".equals(user.getRole())) {
            return "redirect:/auth/login";
        }

        Delivery d = repo.findById(id).orElseThrow();
        d.setStatus(status);
        repo.save(d);

        return "redirect:/admin?status=PENDING";

    }

    @GetMapping("/download/{id}")
    public ResponseEntity<FileSystemResource> download(@PathVariable Long id, HttpSession session) throws Exception {

        User user = (User) session.getAttribute("user");
        if (user == null || !"ROLE_ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).build();
        }

        Delivery d = repo.findById(id).orElseThrow();
        File file = waypointService.generate(d.getId());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + file.getName())
                .body(new FileSystemResource(file));
    }


    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"ROLE_ADMIN".equals(currentUser.getRole())) {
            return "redirect:/home";
        }

        List<User> users = userRepo.findByIdNot(currentUser.getId());

        model.addAttribute("users", users);
        model.addAttribute("user", currentUser);

        return "admin/user-management";
    }

    @PostMapping("/users/{id}/assign-admin")
    public String assignAdmin(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setRole("ROLE_ADMIN");
        userRepo.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/remove-admin")
    public String removeAdmin(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setRole("ROLE_USER");
        userRepo.save(user);
        return "redirect:/admin/users";
    }

}
