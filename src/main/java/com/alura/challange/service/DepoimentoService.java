package com.alura.challange.service;

import com.alura.challange.model.Depoimento;
import com.alura.challange.records.DepoimentosDTO;
import com.alura.challange.repository.DepoimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepoimentoService {

    @Autowired
    private DepoimentoRepository depoimentoRepository;


    public List<DepoimentosDTO> listarDepoimentos() {
        return depoimentoRepository.findAll()
                .stream()
                .map(DepoimentosDTO::new).toList();
    }

    public DepoimentosDTO getDepoimentoById(Long id) {
        var depoimento = depoimentoRepository.getReferenceById(id);
        return new DepoimentosDTO(depoimento);
    }


    public Depoimento updateDepoimento(DepoimentosDTO depoimento) {

        return depoimentoRepository.findById(depoimento.id()).map(d -> {
            d.updateDepoimento(depoimento);
            return d;
        }).orElseThrow(() -> new EntityNotFoundException("Depoimento não encontrado! "));
    }

    public void createDepoimento(Depoimento depoimento) {
        depoimentoRepository.save(depoimento);
    }

    public void deleteDepoimento(Long id) {
        depoimentoRepository.deleteById(id);
    }
}
