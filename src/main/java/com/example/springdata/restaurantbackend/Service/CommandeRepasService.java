package com.example.springdata.restaurantbackend.Service;

import com.example.springdata.restaurantbackend.Entity.CommandeRepas;
import com.example.springdata.restaurantbackend.Repository.CommandeRepasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeRepasService {

    @Autowired
    private CommandeRepasRepository commandeRepasRepository;

    public List<CommandeRepas> getAllCommandesRepas() {
        return commandeRepasRepository.findAll();
    }

    public CommandeRepas getCommandeRepasById(Long id) {
        return commandeRepasRepository.findById(id).orElse(null);
    }

    public CommandeRepas saveCommandeRepas(CommandeRepas commandeRepas) {
        return commandeRepasRepository.save(commandeRepas);
    }

    public void deleteCommandeRepas(Long id) {
        commandeRepasRepository.deleteById(id);
    }

    public CommandeRepas updateCommandeRepas(CommandeRepas commandeRepas) {
        return commandeRepasRepository.save(commandeRepas);
    }
}