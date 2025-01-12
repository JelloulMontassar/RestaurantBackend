package com.example.springdata.restaurantbackend.Requests;

import com.example.springdata.restaurantbackend.DTO.MenuDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuResponse {
    private String message;
    private MenuDTO menu;

}
