package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.DTO.MenuDTO;
import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Entity.Menu;
import com.example.springdata.restaurantbackend.Entity.MenuPlanificationRequest;
import com.example.springdata.restaurantbackend.Entity.Repas;
import com.example.springdata.restaurantbackend.Mapper.MenuMapper;
import com.example.springdata.restaurantbackend.Mapper.RepasMapper;
import com.example.springdata.restaurantbackend.Repository.MenuRepository;
import com.example.springdata.restaurantbackend.Repository.RepasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RepasRepository repasRepository;

    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuMapper::toDTO)
                .peek(dto -> System.out.println("Mapped DTO: " + dto))
                .collect(Collectors.toList());
    }

    public MenuDTO getMenuById(Long id) {
        return MenuMapper.toDTO(menuRepository.findById(id).orElse(null));
    }

    public MenuDTO saveMenu(MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setDate(menuDTO.getDate());

        // Charger les entités `Repas` depuis la base
        List<Repas> repas = repasRepository.findAllById(
                menuDTO.getRepas().stream()
                        .map(RepasDTO::getId)
                        .collect(Collectors.toList())
        );

        menu.setRepas(repas);
        return MenuMapper.toDTO(menuRepository.save(menu));
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public MenuDTO updateMenu(Long id, MenuDTO menuDTO) {
        Menu existingMenu = menuRepository.findById(id).orElse(null);
        if (existingMenu != null) {
            existingMenu.setDate(menuDTO.getDate());

            // Mise à jour des relations Many-to-Many
            List<Repas> repas = repasRepository.findAllById(
                    menuDTO.getRepas().stream()
                            .map(RepasDTO::getId)
                            .collect(Collectors.toList())
            );
            existingMenu.setRepas(repas);

            return MenuMapper.toDTO(menuRepository.save(existingMenu));
        }
        return null;
    }

    public List<Menu> planifierMenusHebdomadairesAvecJson(List<MenuPlanificationRequest> menuRequests) {
        List<Menu> menus = new ArrayList<>();

        for (MenuPlanificationRequest request : menuRequests) {
            Menu menu = new Menu();
            menu.setDate(request.getDate().atStartOfDay());

            // Charger les repas depuis la base en utilisant les IDs
            List<Repas> repas = repasRepository.findAllById(request.getRepasIds());

            if (repas.size() != request.getRepasIds().size()) {
                throw new IllegalArgumentException("Certains repas fournis n'existent pas dans la base de données.");
            }

            menu.setRepas(repas);
            menus.add(menu);
        }

        return menuRepository.saveAll(menus);
    }

}