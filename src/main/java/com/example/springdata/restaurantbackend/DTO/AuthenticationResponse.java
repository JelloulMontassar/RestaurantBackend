package com.example.springdata.restaurantbackend.DTO;

import com.example.springdata.restaurantbackend.Enums.RoleUtilisateur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String messageResponse;
    private RoleUtilisateur role;
    private String email;
    private Long id;
}
