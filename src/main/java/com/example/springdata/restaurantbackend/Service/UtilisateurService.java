package com.example.springdata.restaurantbackend.Service;
import com.example.springdata.restaurantbackend.DTO.AuthenticationRequest;
import com.example.springdata.restaurantbackend.DTO.AuthenticationResponse;
import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;
import com.example.springdata.restaurantbackend.Enums.RoleUtilisateur;
import com.example.springdata.restaurantbackend.Mapper.UtilisateurMapper;
import com.example.springdata.restaurantbackend.Repository.UtilisateurRepository;
import com.example.springdata.restaurantbackend.exception.UserException;
import com.example.springdata.restaurantbackend.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public UtilisateurService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public List<UtilisateurDTO> getAllUsers() {

    return utilisateurRepository.findAll()
                .stream()
                .map(UtilisateurMapper::toDTO)
                .collect(Collectors.toList());
    }
    public UtilisateurDTO saveUtilisateur(Utilisateur utilisateur) {
        return UtilisateurMapper.toDTO(utilisateurRepository.save(utilisateur));
    }
    public UtilisateurDTO getUtilisateurById(Long id) {
        return UtilisateurMapper.toDTO(utilisateurRepository.findById(id).orElse(null));
    }
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
    public UtilisateurDTO updateUtilisateur(Long id, Utilisateur utilisateur) {
        Utilisateur existingUtilisateur = utilisateurRepository.findById(id).orElse(null);
        if (existingUtilisateur != null) {
            existingUtilisateur.setNomUtilisateur(utilisateur.getNomUtilisateur());
            existingUtilisateur.setPrenomUtilisateur(utilisateur.getPrenomUtilisateur());
            existingUtilisateur.setGenre(utilisateur.getGenre());
            existingUtilisateur.setEmail(utilisateur.getEmail());
            existingUtilisateur.setRole(utilisateur.getRole());
            return UtilisateurMapper.toDTO(utilisateurRepository.save(existingUtilisateur));
        }
        return null;
    }
    public UtilisateurDTO getUserByEmail(String email) {
        return UtilisateurMapper.toDTO(utilisateurRepository.findByEmail(email).orElse(null));
    }
    public UtilisateurDTO saveUtilisateurAdmin(UtilisateurDTO utilisateurDTO, String password) {
        Utilisateur utilisateur = UtilisateurMapper.toEntity(utilisateurDTO);
        utilisateur.setMotDePasse(password);
        utilisateur.setRole(RoleUtilisateur.ADMINISTRATEUR);
        return UtilisateurMapper.toDTO(utilisateurRepository.save(utilisateur));
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {

            throw new UserException(e.getMessage());
        }
        Utilisateur user = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));
        Map<String, String> map = new HashMap<>();
        map.put("role", user.getRole().name());
        map.put("id", user.getId().toString());
        map.put("email", user.getEmail().toString());
        String jwtToken = jwtService.genToken(user, map);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .email(user.getEmail())

                .id(user.getId())
                .messageResponse("You have been successfully authenticated!")
                .build();
    }
    public Utilisateur getUserById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

}
