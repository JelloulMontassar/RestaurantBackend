package com.example.springdata.restaurantbackend.Controller;
import com.example.springdata.restaurantbackend.DTO.AuthenticationRequest;
import com.example.springdata.restaurantbackend.DTO.AuthenticationResponse;
import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;
import com.example.springdata.restaurantbackend.Service.UtilisateurService;
import com.example.springdata.restaurantbackend.exception.UserException;
import jakarta.validation.Valid;
import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@Controller
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;
    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAllUsers() {
        return ResponseEntity.ok(utilisateurService.getAllUsers());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request, BindingResult result) {
        System.out.println(result.getAllErrors());
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AuthenticationResponse.builder()
                            .messageResponse("Validation failed, Auth error")
                            .build());
        }
        try {
            AuthenticationResponse response = utilisateurService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (UserException e) {
            log.error("Authentication error: {}", e.getMessage());
            if ("Bad credentials".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(AuthenticationResponse.builder()
                                .messageResponse("User not foundd")
                                .build());
            } else if ("User is disabled".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(AuthenticationResponse.builder()
                                .messageResponse("User account is not active. Please confirm your email.")
                                .build());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(AuthenticationResponse.builder()
                                .messageResponse("An error occurred during authentication.")
                                .build());
            }
        }
    }
    @PostMapping("/ajouter")
    public ResponseEntity<UtilisateurDTO> saveUtilisateur(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.saveUtilisateur(utilisateur));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateurById(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getUtilisateurById(id));
    }
    @PostMapping("/supprimer/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/modifier/{id}")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok(utilisateurService.updateUtilisateur(id, utilisateur));
    }



}
