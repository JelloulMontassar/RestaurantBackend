package com.example.springdata.restaurantbackend.Controller;


import com.example.springdata.restaurantbackend.DTO.MenuDTO;
import com.example.springdata.restaurantbackend.Entity.Menu;
import com.example.springdata.restaurantbackend.Requests.MenuPlanificationRequest;
import com.example.springdata.restaurantbackend.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<Menu>> planifierMenusHebdomadaires(@RequestBody List<MenuPlanificationRequest> menuRequests) {
        try {
            System.out.println("Reçu: " + menuRequests);
            List<Menu> menusHebdomadaires = menuService.planifierMenusHebdomadairesAvecJson(menuRequests);
            return ResponseEntity.ok(menusHebdomadaires);
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }



}
