package com.example.springdata.restaurantbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {

    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<RepasDTO> repas;
}