package com.alura.challange.repository;

import com.alura.challange.model.Destino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DestinosRepository extends JpaRepository<Destino, Long> {

    @Query("""
            select d from Destino d where d.nome LIKE %:nome%
            """)
    List<Destino> buscaDestinoPorNome(String nome);
}
