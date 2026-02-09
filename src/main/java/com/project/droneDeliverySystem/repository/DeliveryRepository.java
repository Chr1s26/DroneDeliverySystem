package com.project.droneDeliverySystem.repository;

import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByUser_Id(Long userId);
    List<Delivery> findByUser(User user);
    long countByStatus(String status);
    List<Delivery> findByStatusOrderByIdDesc(String status);
}

