package com.alura.challange.records;

import com.alura.challange.model.Destino;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DestinosDTO(
        Long id,
        String foto1,
        String foto2,
        @NotBlank
        String nome,
//        @NotNull
//        Double preco,
        String textoDescritivo) {


    public DestinosDTO(Destino destino) {
        this(destino.getId(),destino.getFoto1(), destino.getFoto2(), destino.getNome(), destino.getTextoDescritivo());
    }

}
