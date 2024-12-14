package com.example.springdata.restaurantbackend.DTO;

import com.example.springdata.restaurantbackend.Enums.Genre;
import com.example.springdata.restaurantbackend.Enums.RoleUtilisateur;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDTO {

    private Long id;

    private String nomUtilisateur;

    private String prenomUtilisateur;

    private Genre genre;

    private String email;

    private RoleUtilisateur role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
