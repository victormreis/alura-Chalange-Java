package com.alura.challange.controller;

import com.alura.challange.model.Depoimento;
import com.alura.challange.records.DepoimentosDTO;
import com.alura.challange.records.DepoimentosRequestDTO;
import com.alura.challange.records.DepoimentosUpdateDTO;
import com.alura.challange.service.DepoimentoService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequestMapping("/depoimentos")
public class DepoimentoController {

    private final DepoimentoService depoimentoService;


    public DepoimentoController(DepoimentoService depoimentoService) {
        this.depoimentoService = depoimentoService;
    }


    @GetMapping
    public ResponseEntity getDepoimentos() {

        var depoimentos = depoimentoService.listarDepoimentos();

        return ResponseEntity.ok(depoimentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDepoimentoById(@PathVariable Long id) {
        var depoimento = depoimentoService.getDepoimentoById(id);

        return ResponseEntity.ok(depoimento);
    }


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity updateDepoimento(@ModelAttribute @Valid DepoimentosUpdateDTO depoimento) {
        var depoimentoUpdated = depoimentoService.updateDepoimento(depoimento);
        return ResponseEntity.ok(new DepoimentosDTO(depoimentoUpdated));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity createDepoimento(@ModelAttribute @Valid DepoimentosRequestDTO depoimento,
                                           UriComponentsBuilder uriComponentsBuilder) throws IOException {

        var newDepoimento = depoimentoService.createDepoimento(depoimento);

        var uri = uriComponentsBuilder.path("/depoimentos/{id}").buildAndExpand(newDepoimento.getId()).toUri();
//        depoimentoService.createDepoimento(newDepoimento);
        return ResponseEntity.created(uri).body(new DepoimentosDTO(newDepoimento));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteDepoimento(@PathVariable Long id) {

        depoimentoService.deleteDepoimento(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/home")
    public ResponseEntity getRandomDepoimentos() {
        return ResponseEntity.ok(depoimentoService.getRandomDepoimento().stream().map(DepoimentosDTO::new));
    }
}
