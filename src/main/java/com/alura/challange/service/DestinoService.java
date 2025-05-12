package com.alura.challange.service;

import com.alura.challange.config.errorHandling.ErrorHandlingValidation;
import com.alura.challange.model.Destino;
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

    public DestinosDTO getDestinoById(Long id) {

        var destino = destinosRepository.findById(id).orElseThrow(() -> new ErrorHandlingValidation("Id not found!"));

        return new DestinosDTO(destino);
    }

    public void createDestino(Destino destino) {

         destinosRepository.save(destino);

    }

    public Destino updateDestino(DestinosDTO destinosDTO) {

        return destinosRepository.findById(destinosDTO.id()).map(d -> {
                    d.updateDestino(destinosDTO);
                    return d;
                })
                .orElseThrow(()-> new ErrorHandlingValidation("Id " +
                "invalid"));
    }

    public void deleteDestino(Long id) {

        var destino = destinosRepository.findById(id).orElseThrow(() -> new ErrorHandlingValidation("id not found!"));

        destino.deleteDestino();
    }
}
