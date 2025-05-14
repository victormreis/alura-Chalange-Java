package com.alura.challange.records;

import com.alura.challange.model.Destino;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DestinosDTO(
        Long id,
        String foto,
        @NotBlank
        String nome,
        @NotNull
        Double preco) {


    public DestinosDTO(Destino destino) {
        this(destino.getId(),destino.getFoto(), destino.getNome(), destino.getPreco());
    }

}
