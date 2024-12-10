package com.example.springdata.restaurantbackend.DAO;

import com.example.springdata.restaurantbackend.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDAO extends JpaRepository<Menu, Integer> {
}
