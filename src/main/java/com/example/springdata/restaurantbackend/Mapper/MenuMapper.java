package com.example.springdata.restaurantbackend.Mapper;

import com.example.springdata.restaurantbackend.DTO.MenuDTO;
import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Entity.Menu;
import com.example.springdata.restaurantbackend.Entity.Repas;
import com.example.springdata.restaurantbackend.Repository.RepasRepository;


import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {
    public static MenuDTO toDTO(Menu menu) {
        if (menu == null) {
            return null;
        }
        return new MenuDTO(
                menu.getId(),
                menu.getDate(),
                menu.getRepas().stream()
                        .map(RepasMapper::toDTO) // Conversion des repas en DTO
                        .collect(Collectors.toList())
        );
    }

    public static Menu toEntity(MenuDTO menuDTO, RepasRepository repasRepository) {
        if (menuDTO == null) return null;

        Menu menu = new Menu();
        menu.setId(menuDTO.getId());
        menu.setDate(menuDTO.getDate());

        // Charger les repas depuis la base pour éviter des duplications dans la relation Many-to-Many
        List<Repas> repas = repasRepository.findAllById(
                menuDTO.getRepas().stream()
                        .map(RepasDTO::getId)
                        .collect(Collectors.toList())
        );
        menu.setRepas(repas);
        return menu;
    }

}
