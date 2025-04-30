package com.alura.challange.repository;

import com.alura.challange.model.Depoimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepoimentoRepository extends JpaRepository <Depoimento, Long> {

    @Query(value = "SELECT * FROM depoimentos ORDER BY rand() LIMIT 3;", nativeQuery = true)
    public List<Depoimento> findRandomDepoimento();
}
