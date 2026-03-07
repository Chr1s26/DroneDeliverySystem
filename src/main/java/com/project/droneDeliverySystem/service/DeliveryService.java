package com.project.droneDeliverySystem.service;

import com.project.droneDeliverySystem.dto.DeliveryDto;
import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.entity.User;
import com.project.droneDeliverySystem.exception.ResourceNotFoundException;
import com.project.droneDeliverySystem.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository repo;

    public void createDelivery(DeliveryDto dto, User user){
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
    }

    public List<Delivery> findByStatusOrderById(String status){
        return repo.findByStatusOrderByIdDesc(status);
    }

    public Long countByPendingStatus(){
        return repo.countByStatus("PENDING");
    }

    public Delivery findById(Long id){
        Delivery d = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));
        return d;
    }

    public List<Delivery> findByUserId(Long userId){
        return repo.findByUser_Id(userId);
    }

    public void updateStatus(Long id,String status){
        Delivery d = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));
        d.setStatus(status);
        repo.save(d);
    }
}
