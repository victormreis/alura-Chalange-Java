package com.alura.challange.records;

import com.alura.challange.model.Depoimento;

public record DepoimentosDTO(Long id, String foto, String depoimento, String autor) {
    public DepoimentosDTO(Depoimento depoimento) {
        this(depoimento.getId(), depoimento.getFoto(), depoimento.getDepoimento(), depoimento.getAutor());
    }
}
