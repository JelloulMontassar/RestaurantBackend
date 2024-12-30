package com.example.springdata.restaurantbackend.Entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MenuPlanificationRequest {
    private LocalDate date;
    private List<Long> repasIds;
}
