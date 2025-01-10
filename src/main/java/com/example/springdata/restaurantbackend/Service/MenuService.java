package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.DTO.MenuDTO;
import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Entity.Menu;
import com.example.springdata.restaurantbackend.Requests.MenuPlanificationRequest;
import com.example.springdata.restaurantbackend.Entity.Repas;
import com.example.springdata.restaurantbackend.Mapper.MenuMapper;
import com.example.springdata.restaurantbackend.Repository.MenuRepository;
import com.example.springdata.restaurantbackend.Repository.RepasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
        menu.setStartDate(menuDTO.getStartDate());
        menu.setEndDate(menuDTO.getEndDate());

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
            existingMenu.setStartDate(menuDTO.getStartDate());
            existingMenu.setEndDate(menuDTO.getEndDate());

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
            LocalDate currentDate = request.getStartDate();
            List<Repas> repas = new ArrayList<>();


            for (int i = 0; i < 7; i++) {
                if (i >= request.getRepasIds().size()) {
                    throw new IllegalArgumentException("Données manquantes pour le jour " + (i + 1));
                }

                List<Long> repasIdsForDay = request.getRepasIds().get(i);
                List<Repas> repasForDay = repasRepository.findAllById(repasIdsForDay);

                if (repasForDay.size() != repasIdsForDay.size()) {
                    throw new IllegalArgumentException("Certains repas fournis n'existent pas dans la base de données.");
                }

                repas.addAll(repasForDay);
            }


            Menu menu = new Menu();
            menu.setStartDate(request.getStartDate().atStartOfDay());
            menu.setEndDate(request.getEndDate().atTime(23, 59, 59));
            menu.setRepas(repas);

            menus.add(menu);
        }

        return menuRepository.saveAll(menus);
    }

    public List<Long> getRepasIdsForDate(LocalDate date) {
        return menuRepository.findAll().stream()
                .filter(menu -> !date.isBefore(ChronoLocalDate.from(menu.getStartDate())) && !date.isAfter(ChronoLocalDate.from(menu.getEndDate())))
                .flatMap(menu -> menu.getRepas().stream().map(Repas::getId))
                .collect(Collectors.toList());
    }

    public boolean isRepasAvailableInMenuForDate(Long repasId, LocalDate date) {
        List<Long> repasIdsForDate = getRepasIdsForDate(date);
        return repasIdsForDate.contains(repasId);
    }

}