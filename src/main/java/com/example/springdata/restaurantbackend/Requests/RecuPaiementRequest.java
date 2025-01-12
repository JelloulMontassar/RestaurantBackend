package com.example.springdata.restaurantbackend.Requests;


import lombok.Data;

@Data
public class RecuPaiementRequest {
    private Long utilisateurId;
    private String datePaiement;
}
