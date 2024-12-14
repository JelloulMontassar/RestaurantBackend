package com.example.springdata.restaurantbackend.Service;
import com.example.springdata.restaurantbackend.DTO.UtilisateurDTO;
import com.example.springdata.restaurantbackend.Entity.Utilisateur;
import com.example.springdata.restaurantbackend.Mapper.UtilisateurMapper;
import com.example.springdata.restaurantbackend.Repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    public List<UtilisateurDTO> getAllUsers() {
        return utilisateurRepository.findAll().stream().map(UtilisateurMapper::toDTO).collect(Collectors.toList());
    }
    public UtilisateurDTO saveUtilisateur(Utilisateur utilisateur) {
        return UtilisateurMapper.toDTO(utilisateurRepository.save(utilisateur));
    }

}
