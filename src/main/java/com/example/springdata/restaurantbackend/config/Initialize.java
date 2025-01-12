package com.example.springdata.restaurantbackend.config;

import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Enums.Genre;
import com.example.springdata.restaurantbackend.Service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initialize implements CommandLineRunner {

    private final UtilisateurService userService;
    private final BCryptPasswordEncoder bCryptEncoder;
    @Override
    public void run(String... args) throws Exception {
        try {
            createAdminUserIfNeeded();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private void createAdminUserIfNeeded() {
        try {
            UtilisateurDTO adminUser = userService.getUserByEmail("admin1@admin.com");
            if (adminUser == null) {
                adminUser = UtilisateurDTO.builder()
                        .prenomUtilisateur("admin")
                        .nomUtilisateur("admin")
                        .email("admin1@admin.com")
                        .nomUtilisateur("ADMIN")
                        .genre(Genre.MASCULIN)

                        .build();
                String password = bCryptEncoder.encode("admin123");
                userService.saveUtilisateurAdmin(adminUser, password);
            }
        } catch (Exception e) {
            UtilisateurDTO adminUser = UtilisateurDTO.builder()
                    .nomUtilisateur("admin")
                    .prenomUtilisateur("admin")
                    .email("admin@admin.com")
                    .nomUtilisateur("ADMIN")
                    .genre(Genre.MASCULIN)

                    .build();
            userService.saveUtilisateurAdmin(adminUser, "admin123");
        }
    }
}
