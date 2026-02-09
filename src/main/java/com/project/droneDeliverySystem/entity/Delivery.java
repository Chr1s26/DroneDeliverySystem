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

    // ===== Sender / Receiver =====
    private String senderName;
    private String receiverName;
    private String receiverEmail;
    private String receiverPhone;

    // ===== Location =====
    private double sourceLat;
    private double sourceLng;
    private double destLat;
    private double destLng;

    // ===== Status =====
    private String status;

    // ===== User =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

