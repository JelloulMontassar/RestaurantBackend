package com.example.springdata.restaurantbackend.Requests;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MenuPlanificationRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<List<Long>> repasIds;
}
