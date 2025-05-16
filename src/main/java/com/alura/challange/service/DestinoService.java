package com.alura.challange.service;

import com.alura.challange.config.errorHandling.ErrorHandlingValidation;
import com.alura.challange.model.Destino;
import com.alura.challange.records.DestinosDTO;
import com.alura.challange.records.DestinosDTORequest;
import com.alura.challange.repository.DestinosRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DestinoService {

    private final DestinosRepository destinosRepository;

    private final GeminiService aiService;

    private final S3Service s3Service;

    public DestinoService(DestinosRepository destinosRepository, GeminiService geminiService, S3Service s3Service) {
        this.destinosRepository = destinosRepository;
        this.aiService = geminiService;
        this.s3Service = s3Service;
    }


    public List<DestinosDTO> getDestinos(String nome) {

        if (nome != null && !nome.isBlank()) {
            return getDestinosByName(nome);
        }

        return destinosRepository.findAll().stream().map(DestinosDTO::new).toList();

    }

    public DestinosDTO getDestinoById(Long id) {

        var destino = destinosRepository.findById(id).orElseThrow(() -> new ErrorHandlingValidation("Id not found!"));

        return new DestinosDTO(destino);
    }

    public Destino createDestino(DestinosDTORequest dto) throws IOException {
        String foto1Url = "";
        String foto2Url = "";


        if (!dto.foto1().isEmpty()) {
            foto1Url = s3Service.uploadFile(dto.foto1());
        }

        if (!dto.foto2().isEmpty()) {
            foto2Url = s3Service.uploadFile(dto.foto2());
        }

        DestinosDTO destinosDTO = new DestinosDTO(null, foto1Url, foto2Url, dto.nome(), dto.textoDescritivo());


        Destino destino = new Destino(destinosDTO);

        if (dto.textoDescritivo().isEmpty()) {
            destino.setTextoDescritivo(aiService.generateDescription(destino.getNome()));
        }

        return destinosRepository.save(destino);

    }



    public Destino updateDestino(DestinosDTO destinosDTO) {


        return destinosRepository.findById(destinosDTO.id()).map(d -> {
                    d.updateDestino(destinosDTO);
                    if (destinosDTO.textoDescritivo() != null && destinosDTO.textoDescritivo().isEmpty()) {
                        d.setTextoDescritivo(aiService.generateDescription(d.getNome()));
                    }
                    return d;
                })
                .orElseThrow(() -> new ErrorHandlingValidation("Id " +
                        "invalid"));
    }

    public void deleteDestino(Long id) {

        var destino = destinosRepository.findById(id).orElseThrow(() -> new ErrorHandlingValidation("id not found!"));

        destino.deleteDestino();
    }

    public List<DestinosDTO> getDestinosByName(String nome) {
        var destinosDto = destinosRepository.buscaDestinoPorNome(nome).stream().map(d ->
                new DestinosDTO(d)).toList();

        return destinosDto;
    }
}
