package com.project.droneDeliverySystem.repository;

import com.project.droneDeliverySystem.entity.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByIdNot(Long id);

    boolean existsByEmailAndIdNot(String email, Long userId);
}

