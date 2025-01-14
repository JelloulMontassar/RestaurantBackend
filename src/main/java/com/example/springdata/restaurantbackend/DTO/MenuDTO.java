package com.example.springdata.restaurantbackend.DTO;

import com.example.springdata.restaurantbackend.Enums.MenuType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {

    private Long id;
    
    private MenuType type;

    @FutureOrPresent
    private LocalDate startDate;

    @Future
    private LocalDate endDate;

    private List<RepasDTO> repas;
}
