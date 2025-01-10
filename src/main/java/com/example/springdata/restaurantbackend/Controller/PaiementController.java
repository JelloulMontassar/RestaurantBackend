package com.example.springdata.restaurantbackend.Controller;


import com.example.springdata.restaurantbackend.Entity.Paiement;
import com.example.springdata.restaurantbackend.Service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/paiements")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @GetMapping
    public List<Paiement> getPaiements(
            @RequestParam Long utilisateurId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        LocalDateTime startDateTime = (startDate != null) ? parseDate(startDate, true) : null;
        LocalDateTime endDateTime = (endDate != null) ? parseDate(endDate, false) : null;

        // Appel du service pour récupérer les paiements
        return paiementService.getPaiementsByUtilisateur(utilisateurId, startDateTime, endDateTime);
    }

    private LocalDateTime parseDate(String dateStr, boolean isStartOfDay) {
        if (dateStr.length() == 10) { // Format: yyyy-MM-dd
            LocalDate date = LocalDate.parse(dateStr);
            return isStartOfDay ? date.atStartOfDay() : date.atTime(23, 59, 59);
        }
        return LocalDateTime.parse(dateStr); // Format complet
    }

}
