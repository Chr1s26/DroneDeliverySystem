package com.project.droneDeliverySystem.service;

import com.project.droneDeliverySystem.dto.UserProfileDto;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.ResourceNotFoundException;
import com.project.droneDeliverySystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;

    public void updateUser(User user, UserProfileDto dto){
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        userRepo.save(user);
    }

    public User findById(Long id){
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found when editing profile"));
    }

    public List<User> findByIdNot(Long id){
        return userRepo.findByIdNot(id);
    }

    public boolean existsByEmailAndIdNot(String email, Long id){
        return userRepo.existsByEmailAndIdNot(email, id);
    }

    public void assignAdmin(Long id){
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found when assigning admin"));
        user.setRole("ROLE_ADMIN");
        userRepo.save(user);
    }

    public void assignUser(Long id){
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found when assigning admin"));
        user.setRole("ROLE_USER");
        userRepo.save(user);
    }
}
