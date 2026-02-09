package com.project.droneDeliverySystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String packageName;
    private String packageDescription;
    private String senderName;
    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;
    private double sourceLat;
    private double sourceLng;
    private double destLat;
    private double destLng;
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

