package com.example.springdata.restaurantbackend.DAO;

import com.example.springdata.restaurantbackend.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationDAO extends JpaRepository<Notification, Integer> {
}
