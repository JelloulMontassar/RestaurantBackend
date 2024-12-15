/*package com.example.springdata.restaurantbackend.Mapper;

import com.example.springdata.restaurantbackend.DTO.MenuDTO;
import com.example.springdata.restaurantbackend.Entity.Menu;

public class MenuMapper {
    public static MenuDTO toDTO(Menu menu){
        if (menu == null){
            return null;
        }
        return new MenuDTO(
                menu.getId(),
                menu.getDate()
                RepasMapper.toDTO(menu.getRepas())
        );

    }
    public static Menu toEntity(MenuDTO menuDTO){
        if (menuDTO == null){
            return null;
        }
        return new Menu(
                menuDTO.getId(),
                menuDTO.getDate(),
                RepasMapper.toEntity(menuDTO.getRepas())
        );
    }
}
*/