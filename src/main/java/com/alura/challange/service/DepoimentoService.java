package com.alura.challange.service;

import com.alura.challange.config.errorHandling.ErrorHandlingValidation;
import com.alura.challange.model.Depoimento;
import com.alura.challange.records.DepoimentosDTO;
import com.alura.challange.records.DepoimentosRequestDTO;
import com.alura.challange.records.DepoimentosUpdateDTO;
import com.alura.challange.repository.DepoimentoRepository;
import com.alura.challange.repository.DestinosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DepoimentoService {

    @Autowired
    private DepoimentoRepository depoimentoRepository;

    @Autowired
    private DestinosRepository destinosRepository;

    @Autowired
    private S3Service s3Service;


    public List<DepoimentosDTO> listarDepoimentos() {
        return depoimentoRepository.findAll()
                .stream()
                .map(DepoimentosDTO::new).toList();
    }

    public DepoimentosDTO getDepoimentoById(Long id) {
        var depoimento = depoimentoRepository.getReferenceById(id);
        return new DepoimentosDTO(depoimento);
    }


    public Depoimento updateDepoimento(DepoimentosUpdateDTO depoimento) {

        var depoimentoExistente =
                depoimentoRepository.findById(depoimento.id()).orElseThrow(() -> new ErrorHandlingValidation(
                        "DepoimentoId not found"));

        var destino = destinosRepository.findById(depoimento.destinoId()).orElseThrow(() -> new ErrorHandlingValidation(
                "DestinoId not found"));

        String fotourl = depoimentoExistente.getFoto();

        if (depoimento.foto() != null) {
            try {
                fotourl = s3Service.uploadFile(depoimento.foto());
            } catch (IOException e) {
                throw new ErrorHandlingValidation("Error uploading photo");
            }
        }

        var depoimentoAtt = new DepoimentosDTO(
                depoimento.id(),
                fotourl,
                depoimento.depoimento(),
                depoimento.autor(),
                destino.getId()
        );

        depoimentoExistente.updateDepoimento(depoimentoAtt);

        return depoimentoExistente;
    }

    public Depoimento createDepoimento(DepoimentosRequestDTO dto) throws IOException {
        String fotourl = "";
        if (!dto.foto().isEmpty()) {
            fotourl = s3Service.uploadFile(dto.foto());
        }
        var destino =
                destinosRepository.findById(dto.destinoId()).orElseThrow(() -> new ErrorHandlingValidation("Id not " +
                        "Found!"));

        var depoimento = new Depoimento(null, dto.depoimento(), dto.autor(), fotourl, destino);
        depoimento.setDestino(destino);
        depoimento.setFoto(fotourl);
        return depoimentoRepository.save(depoimento);
    }

    public void deleteDepoimento(Long id) {
        depoimentoRepository.deleteById(id);
    }


    public List<Depoimento> getRandomDepoimento() {
        return depoimentoRepository.findRandomDepoimento();
    }

}
