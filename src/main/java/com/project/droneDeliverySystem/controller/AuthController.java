package com.project.droneDeliverySystem.controller;

import com.project.droneDeliverySystem.dto.EmailRequest;
import com.project.droneDeliverySystem.dto.RegisterRequest;
import com.project.droneDeliverySystem.dto.ResetPasswordDTO;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.OtpExpiredException;
import com.project.droneDeliverySystem.exception.OtpInvalidException;
import com.project.droneDeliverySystem.repository.UserRepository;
import com.project.droneDeliverySystem.service.AuthService;
import com.project.droneDeliverySystem.service.OTPService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthService authService;

    @Autowired
    OTPService otpService;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @GetMapping("/login")
    public String login(Model model) {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        User user = userRepo.findByEmail(email).orElse(null);

        if (user == null || !encoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Invalid login !!");
            return "auth/login";
        }

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(user.getRole());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        List.of(authority)
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        securityContextRepository.saveContext(
                SecurityContextHolder.getContext(),
                request,
                response
        );

        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());

        return "redirect:/home";
    }


    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register( @Valid RegisterRequest request, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error",
                    bindingResult.getFieldError().getDefaultMessage());
            return "auth/register";
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "auth/register";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());

        String result = authService.register(user, request.getConfirmPassword());

        if (!"SUCCESS".equals(result)) {
            model.addAttribute("error", result);
            return "auth/register";
        }

        return "redirect:/auth/login";
    }

    @GetMapping("/forget-password")
    public String showForgetPasswordForm(Model model) {
        model.addAttribute("emailForm", new EmailRequest());
        return "auth/forget-password";
    }

    @PostMapping("/forget-password")
    public String checkEmailProcess(@Valid @ModelAttribute("emailForm") EmailRequest emailForm, HttpSession session, BindingResult bindingResult) {
        Optional<User> userOp = userRepo.findByEmail(emailForm.getEmail());
        if (userOp.isEmpty()) bindingResult.rejectValue("email", "not found", "An account with this email doesn't exist");
        if (bindingResult.hasErrors()) return "auth/forget-password";

        otpService.sendOtp(userOp.get());
        session.setAttribute("resetEmail", emailForm.getEmail());
        return "redirect:/auth/confirm-otp";
    }

    @GetMapping("/confirm-otp")
    public String showConfirmOtpForm() {
        return "auth/confirm-otp";
    }

    @PostMapping("/confirm-otp")
    public String processOtp(@RequestParam("otp") String otp, HttpSession session, Model model) {

        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            model.addAttribute("otpError", "Session expired. Please try again.");
            return "auth/forget-password";
        }

        try{
            otpService.isOtpValid(email, otp);
        }catch (OtpExpiredException e){
            model.addAttribute("otpError", "OTP is expired. Please try again.");
            return "auth/confirm-otp";
        }catch (OtpInvalidException e){
            model.addAttribute("otpError", "Invalid OTP. Please try again.");
            return "auth/confirm-otp";
        }

        return "redirect:/auth/reset-password";
    }

    @PostMapping("/resend-otp")
    public String resendOtp(HttpSession session, Model model) {

        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            model.addAttribute("error", "Session expired. Please start again.");
            return "auth/forget-password";
        }

        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            model.addAttribute("error", "User not found.");
            return "auth/forget-password";
        }

        otpService.sendOtp(user);

        model.addAttribute("otpError", "A new OTP has been sent to your email.");
        return "auth/confirm-otp";
    }


    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model) {
        model.addAttribute("resetPasswordForm", new ResetPasswordDTO());
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("resetPasswordForm") ResetPasswordDTO form, HttpSession session, Model model) {

        String email = (String) session.getAttribute("resetEmail");
        if (email == null) {
            model.addAttribute("error", "Session expired. Please start again.");
            return "auth/forget-password";
        }

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "auth/reset-password";
        }

        authService.resetPassword(email, form.getPassword());
        session.removeAttribute("resetEmail");

        return "redirect:/auth/login";
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }


}

