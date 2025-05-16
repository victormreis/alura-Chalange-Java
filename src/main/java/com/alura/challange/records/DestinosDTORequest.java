package com.alura.challange.records;

import org.springframework.web.multipart.MultipartFile;

public record DestinosDTORequest (
        MultipartFile foto1,
        MultipartFile foto2,
        String nome,
        String textoDescritivo
) {}
