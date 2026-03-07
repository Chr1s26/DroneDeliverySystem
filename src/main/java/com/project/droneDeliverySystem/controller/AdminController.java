package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.ResourceNotFoundException;
import com.project.droneDeliverySystem.repository.DeliveryRepository;
import com.project.droneDeliverySystem.repository.UserRepository;
import com.project.droneDeliverySystem.service.DeliveryService;
import com.project.droneDeliverySystem.service.UserService;
import com.project.droneDeliverySystem.service.WaypointService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final DeliveryService deliveryService;
    private final WaypointService waypointService;
    private final UserService userService;

    @GetMapping
    public String adminPage(@RequestParam(defaultValue = "PENDING") String status, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Delivery> deliveries = deliveryService.findByStatusOrderById(status);
        model.addAttribute("user", user);
        model.addAttribute("deliveries", deliveries);
        model.addAttribute("currentStatus", status);
        model.addAttribute("pendingCount", deliveryService.countByPendingStatus());
        return "admin/deliveries";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, HttpSession session) {
        deliveryService.updateStatus(id,status);
        return "redirect:/api/admin?status=PENDING";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<FileSystemResource> download(@PathVariable Long id, HttpSession session) throws Exception {
        Delivery d = deliveryService.findById(id);
        File file = waypointService.generate(d.getId());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + file.getName())
                .body(new FileSystemResource(file));
    }


    @GetMapping("/users")
    public String listUsers(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"ROLE_ADMIN".equals(currentUser.getRole())) {
            return "redirect:/api/home";
        }

        List<User> users = userService.findByIdNot(currentUser.getId());
        model.addAttribute("users", users);
        model.addAttribute("user", currentUser);
        return "admin/user-management";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{id}/assign-admin")
    public String assignAdmin(@PathVariable Long id) {
        userService.assignAdmin(id);
        return "redirect:/api/admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{id}/remove-admin")
    public String removeAdmin(@PathVariable Long id) {
        userService.assignUser(id);
        return "redirect:/api/admin/users";
    }

}
