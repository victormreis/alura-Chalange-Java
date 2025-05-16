package com.alura.challange.records;

import org.springframework.web.multipart.MultipartFile;

public record DestinosUpdateRequest(
        Long id,
        String nome,
        String textoDescritivo,
        MultipartFile foto1,
        MultipartFile foto2
) {}
