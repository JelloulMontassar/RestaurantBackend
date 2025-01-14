package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.DTO.MenuDTO;
import com.example.springdata.restaurantbackend.DTO.RepasDTO;
import com.example.springdata.restaurantbackend.Entity.Menu;
import com.example.springdata.restaurantbackend.Enums.MenuType;
import com.example.springdata.restaurantbackend.Requests.MenuPlanificationRequest;
import com.example.springdata.restaurantbackend.Entity.Repas;
import com.example.springdata.restaurantbackend.Mapper.MenuMapper;
import com.example.springdata.restaurantbackend.Repository.MenuRepository;
import com.example.springdata.restaurantbackend.Repository.RepasRepository;
import com.example.springdata.restaurantbackend.Requests.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RepasRepository repasRepository;
    public MenuDTO getMenuByDate(LocalDate date) {
        Menu menu = menuRepository.findAll().stream()
                .filter(m -> !date.isBefore(ChronoLocalDate.from(m.getStartDate())) && !date.isAfter(ChronoLocalDate.from(m.getEndDate())))
                .findFirst()
                .orElse(null);

        return MenuMapper.toDTO(menu);
    }
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
            existingMenu.setType(menuDTO.getType() != null ? menuDTO.getType() : existingMenu.getType());

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

    public List<Menu> planifierMenusHebdomadairesAvecJson(MenuPlanificationRequest menuRequests) {
        List<Menu> menus = new ArrayList<>();

            // Vérification du type de menu
            if (menuRequests.getType() != MenuType.HEBDOMADAIRE) {
                throw new IllegalArgumentException("Cette méthode est uniquement destinée à planifier des menus hebdomadaires.");
            }

            LocalDate currentDate = menuRequests.getStartDate();
            List<Repas> repas = new ArrayList<>();

            for (int i = 0; i < 7; i++) {
                if (i >= menuRequests.getRepasIds().size()) {
                    throw new IllegalArgumentException("Données manquantes pour le jour " + (i + 1));
                }

                List<Long> repasIdsForDay = menuRequests.getRepasIds().get(i);
                List<Repas> repasForDay = repasRepository.findAllById(repasIdsForDay);
                
                if (repasForDay.size() != repasIdsForDay.size()) {
                    throw new IllegalArgumentException("Certains repas fournis n'existent pas dans la base de données.");
                }

                repas.addAll(repasForDay);
            }

            // Creating the menu for the week
            Menu menu = new Menu();
            menu.setType(MenuType.HEBDOMADAIRE); // Assurez-vous que le type est défini correctement
            menu.setStartDate(menuRequests.getStartDate());
            menu.setEndDate(menuRequests.getEndDate());
            menu.setRepas(repas);

            menus.add(menu);


        // Save all the menus and return
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


    //menu quotidienne
    public MenuResponse saveDailyMenu(LocalDate date, List<Long> repasIds) {
        // Vérifier si un menu quotidien existe déjà pour cette date
        Menu existingMenu = menuRepository.findAll().stream()
                .filter(menu -> date.isEqual(menu.getStartDate()) &&
                        date.isEqual(menu.getEndDate()) &&
                        menu.getType() == MenuType.QUOTIDIEN) // Assurez-vous qu'il s'agit d'un menu quotidien
                .findFirst()
                .orElse(null);

        if (existingMenu != null) {
            // Si un menu quotidien existe déjà pour la date, renvoyer un message avec les détails
            return new MenuResponse("Un menu quotidien existe déjà pour cette date.", MenuMapper.toDTO(existingMenu));
        }

        // Si aucun menu n'existe, procéder à la création du menu quotidien
        List<Repas> repas = repasRepository.findAllById(repasIds);
        if (repas.size() != repasIds.size()) {
            throw new IllegalArgumentException("Certains repas fournis n'existent pas dans la base de données.");
        }

        Menu menu = new Menu();
        menu.setType(MenuType.QUOTIDIEN); // Assurez-vous que le type est défini correctement
        menu.setStartDate(LocalDate.from(date.atStartOfDay()));
        menu.setEndDate(LocalDate.from(date.atTime(23, 59, 59)));
        menu.setRepas(repas);

        return new MenuResponse("Menu quotidien créé avec succès.", MenuMapper.toDTO(menuRepository.save(menu)));
    }



    //récuppérer menu quotidien
    public MenuDTO getDailyMenu(LocalDate date) {
        Menu menu = menuRepository.findAll().stream()
                .filter(m -> date.isEqual(m.getStartDate()) &&
                        date.isEqual(m.getEndDate()))
                .findFirst()
                .orElse(null);

        return MenuMapper.toDTO(menu);
    }


    // Mise à jour du menu quotidien
    public MenuResponse updateDailyMenu(LocalDate date, List<Long> repasIds) {
        // Recherche du menu quotidien existant pour la date donnée
        Menu existingMenu = menuRepository.findAll().stream()
                .filter(menu -> date.isEqual(menu.getStartDate()) &&
                        date.isEqual(menu.getEndDate()) &&
                        menu.getType() == MenuType.QUOTIDIEN)  // Assurez-vous qu'il s'agit bien d'un menu quotidien
                .findFirst()
                .orElse(null);

        if (existingMenu != null) {
            // Si le menu quotidien existe, on met à jour les repas associés
            List<Repas> repas = repasRepository.findAllById(repasIds);
            if (repas.size() != repasIds.size()) {
                throw new IllegalArgumentException("Certains repas fournis n'existent pas dans la base de données.");
            }

            existingMenu.setRepas(repas);
            // Sauvegarde du menu modifié
            menuRepository.save(existingMenu);

            return new MenuResponse("Menu quotidien mis à jour avec succès.", MenuMapper.toDTO(existingMenu));
        }

        return new MenuResponse("Menu quotidien non trouvé pour cette date.", null);
    }


    // Récupérer tous les menus quotidiens
    public List<MenuDTO> getAllDailyMenus() {
        List<Menu> dailyMenus = menuRepository.findAll().stream()
                .filter(menu -> menu.getType() == MenuType.QUOTIDIEN)
                .collect(Collectors.toList());

        return dailyMenus.stream()
                .map(MenuMapper::toDTO)
                .collect(Collectors.toList());
    }

}
