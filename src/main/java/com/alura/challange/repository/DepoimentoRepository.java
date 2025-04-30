package com.alura.challange.repository;

import com.alura.challange.model.Depoimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepoimentoRepository extends JpaRepository <Depoimento, Long> {
}
