package com.project.droneDeliverySystem.service;

import com.project.droneDeliverySystem.entity.Delivery;
import com.project.droneDeliverySystem.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service
public class WaypointService {

    @Autowired
    DeliveryRepository repo;

    public File generate(Long deliveryId) {
        Delivery d = repo.findById(deliveryId).orElseThrow();
        File file = new File("delivery_" + deliveryId + ".waypoints");

        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println("QGC WPL 110");
            pw.println("0\t1\t0\t16\t0\t0\t0\t0\t" +
                    d.getSourceLat() + "\t" + d.getSourceLng() + "\t10\t1");
            pw.println("1\t0\t0\t16\t0\t0\t0\t0\t" +
                    d.getDestLat() + "\t" + d.getDestLng() + "\t10\t1");
            pw.println("2\t0\t0\t20\t0\t0\t0\t0\t0\t0\t0\t1");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}

