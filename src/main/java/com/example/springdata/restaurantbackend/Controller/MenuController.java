package com.example.springdata.restaurantbackend.Controller;


import com.example.springdata.restaurantbackend.DTO.MenuDTO;
import com.example.springdata.restaurantbackend.Entity.Menu;
import com.example.springdata.restaurantbackend.Enums.MenuType;
import com.example.springdata.restaurantbackend.Requests.DailyMenuRequest;
import com.example.springdata.restaurantbackend.Requests.MenuPlanificationRequest;
import com.example.springdata.restaurantbackend.Requests.MenuResponse;
import com.example.springdata.restaurantbackend.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuDTO>> getAllMenus() {
        List<MenuDTO> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable Long id) {
        MenuDTO menu = menuService.getMenuById(id);
        if (menu != null) {
            return ResponseEntity.ok(menu);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/getMenuByDate")
    public ResponseEntity<MenuDTO> getMenuByDate(@RequestParam LocalDate date) {
        MenuDTO menu = menuService.getMenuByDate(date);
        if (menu != null) {
            return ResponseEntity.ok(menu);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/ajouter")
    public ResponseEntity<MenuDTO> createMenu(@RequestBody MenuDTO menuDTO) {

        MenuDTO savedMenu = menuService.saveMenu(menuDTO);
        return ResponseEntity.ok(savedMenu);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> updateMenu(@PathVariable Long id, @RequestBody MenuDTO menuDTO) {
        MenuDTO updatedMenu = menuService.updateMenu(id, menuDTO);
        if (updatedMenu != null) {
            return ResponseEntity.ok(updatedMenu);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/planifier-hebdomadaire")
    public ResponseEntity<List<Menu>> planifierMenusHebdomadaires(@RequestBody MenuPlanificationRequest menuRequests) {
        try {
            System.out.println("Reçu: " + menuRequests);
            List<Menu> menusHebdomadaires = menuService.planifierMenusHebdomadairesAvecJson(menuRequests);
            return ResponseEntity.ok(menusHebdomadaires);
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    //menu quotidien
    @PostMapping("/quotidien")
    public ResponseEntity<MenuResponse> createDailyMenu(@RequestBody DailyMenuRequest request) {
        try {
            MenuResponse response = menuService.saveDailyMenu(request.getDate(), request.getRepasIds());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MenuResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/quotidien")
    public ResponseEntity<MenuDTO> getDailyMenu(@RequestParam LocalDate date) {
        MenuDTO menuDTO = menuService.getDailyMenu(date);
        if (menuDTO != null) {
            return ResponseEntity.ok(menuDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

// Récupérer tous les menus quotidiens
    @GetMapping("/quotidiens")
    public ResponseEntity<List<MenuDTO>> getAllDailyMenus() {
        List<MenuDTO> dailyMenus = menuService.getAllMenus().stream()
                .filter(menu -> menu.getType() == MenuType.QUOTIDIEN)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dailyMenus);
    }

    // Mettre à jour un menu quotidien
    @PutMapping("/quotidien/{date}")
    public ResponseEntity<MenuResponse> updateDailyMenu(@PathVariable LocalDate date, @RequestBody DailyMenuRequest request) {
        try {
            // Utilisation de la méthode updateDailyMenu pour mettre à jour un menu quotidien
            MenuResponse response = menuService.updateDailyMenu(date, request.getRepasIds());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MenuResponse(e.getMessage(), null));
        }
    }

}
