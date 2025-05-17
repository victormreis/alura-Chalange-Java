package com.alura.challange.records;

import com.alura.challange.model.Depoimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record DepoimentosRequestDTO(
        MultipartFile foto,
        @NotBlank
        String depoimento,
        @NotBlank
        String autor,
        @NotNull
        Long destinoId) {

}
