package com.alura.challange.records;

import com.alura.challange.model.Destino;

public record DestinosDTO(Long id, String foto, String nome, Double preco) {


    public DestinosDTO(Destino destino) {
        this(destino.getId(),destino.getFoto(), destino.getNome(), destino.getPreco());
    }
}
