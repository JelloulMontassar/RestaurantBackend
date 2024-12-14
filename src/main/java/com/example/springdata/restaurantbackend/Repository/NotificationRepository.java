package com.example.springdata.restaurantbackend.Repository;

import com.example.springdata.restaurantbackend.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
