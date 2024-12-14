package com.example.springdata.restaurantbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Long id;
    private String message;
    private boolean lu;
    private UtilisateurDTO utilisateur;
}