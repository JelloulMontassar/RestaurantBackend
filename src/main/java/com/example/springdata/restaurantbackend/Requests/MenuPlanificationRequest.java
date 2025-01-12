package com.example.springdata.restaurantbackend.Requests;

import com.example.springdata.restaurantbackend.Enums.MenuType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MenuPlanificationRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<List<Long>> repasIds;
    private MenuType type;
}
