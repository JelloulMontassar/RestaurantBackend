package com.example.springdata.restaurantbackend.Requests;

import com.example.springdata.restaurantbackend.Enums.TypePaiement;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Getter
@Setter
public class PayerRepasRequest {
    @NotNull
    private Long carteId;

    @NotNull
    private List<Long> repasIds;

    @NotNull
    private TypePaiement typePaiement;

}
