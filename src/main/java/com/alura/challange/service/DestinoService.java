package com.alura.challange.service;

import com.alura.challange.records.DestinosDTO;
import com.alura.challange.repository.DestinosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DestinoService {

    private final DestinosRepository destinosRepository;

    public DestinoService(DestinosRepository destinosRepository) {
        this.destinosRepository = destinosRepository;
    }


    public List<DestinosDTO> getDestinos() {

        return destinosRepository.findAll().stream().map(DestinosDTO::new).toList();

    }
}
