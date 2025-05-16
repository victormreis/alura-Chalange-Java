package com.alura.challange.service;

import com.alura.challange.config.errorHandling.ErrorHandlingValidation;
import com.alura.challange.model.Destino;
import com.alura.challange.records.DestinosDTO;
import com.alura.challange.records.DestinosDTORequest;
import com.alura.challange.records.DestinosUpdateRequest;
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



    public Destino updateDestino(DestinosUpdateRequest request) throws IOException {

        String foto1Url = null;
        String foto2Url = null;

        if (request.foto1() != null && !request.foto1().isEmpty()) {
            foto1Url = s3Service.uploadFile(request.foto1());
        }

        if (request.foto2() != null && !request.foto2().isEmpty()) {
            foto2Url = s3Service.uploadFile(request.foto2());
        }

        Destino destino = destinosRepository.findById(request.id())
                .orElseThrow(() -> new ErrorHandlingValidation("Id invalid"));

        // Usa as imagens existentes se nenhuma nova foi enviada
        var destinoDTO = new DestinosDTO(
                destino.getId(),
                foto1Url != null ? foto1Url : destino.getFoto1(),
                foto2Url != null ? foto2Url : destino.getFoto2(),
                request.nome() != null ? request.nome() : destino.getNome(),
                request.textoDescritivo() != null ? request.textoDescritivo() : destino.getTextoDescritivo()
        );

        destino.updateDestino(destinoDTO);

        if (request.textoDescritivo() != null && request.textoDescritivo().isEmpty()) {
            destino.setTextoDescritivo(aiService.generateDescription(destino.getNome()));
        }

        return destino;
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
