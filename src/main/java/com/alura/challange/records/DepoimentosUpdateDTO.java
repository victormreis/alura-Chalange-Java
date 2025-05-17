package com.alura.challange.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record DepoimentosUpdateDTO(
        @NotNull
        Long id,
        MultipartFile foto,
        String depoimento,
        String autor,
        @NotNull
        Long destinoId
) {
}
