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
                menu.getType(),
                menu.getStartDate(),
                menu.getEndDate(),
                menu.getRepas().stream()
                        .map(RepasMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    public static Menu toEntity(MenuDTO menuDTO, RepasRepository repasRepository) {
        if (menuDTO == null) return null;

        Menu menu = new Menu();
        menu.setId(menuDTO.getId());
        menu.setType(menuDTO.getType());
        menu.setStartDate(menuDTO.getStartDate());
        menu.setEndDate(menuDTO.getEndDate());

        List<Repas> repas = repasRepository.findAllById(
                menuDTO.getRepas().stream()
                        .map(RepasDTO::getId)
                        .collect(Collectors.toList())
        );
        menu.setRepas(repas);
        return menu;
    }

}
