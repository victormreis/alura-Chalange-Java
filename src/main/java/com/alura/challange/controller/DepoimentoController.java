package com.alura.challange.controller;

import com.alura.challange.records.DepoimentosDTO;
import com.alura.challange.records.DepoimentosRequestDTO;
import com.alura.challange.records.DepoimentosUpdateDTO;
import com.alura.challange.service.DepoimentoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity updateDepoimento(
            @RequestParam("id") Long id,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            @RequestParam(value = "depoimento", required = false) String depoimento,
            @RequestParam(value = "autor", required = false) String autor,
            @RequestParam(value = "destinoId", required = false) Long destinoId
    ) {
        var depoimentoDTO = new DepoimentosUpdateDTO(id, foto, depoimento, autor, destinoId);

        var depoimentoUpdated = depoimentoService.updateDepoimento(depoimentoDTO);
        return ResponseEntity.ok(new DepoimentosDTO(depoimentoUpdated));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity createDepoimento(
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            @RequestParam(value = "depoimento", required = true) String depoimento,
            @RequestParam("autor") String autor,
            @RequestParam("destinoId") Long destinoId,
            UriComponentsBuilder uriComponentsBuilder) throws IOException {

                var depoimentoDTO  = new DepoimentosRequestDTO(foto,depoimento,autor,destinoId);

        var newDepoimento = depoimentoService.createDepoimento(depoimentoDTO);

        var uri = uriComponentsBuilder.path("/depoimentos/{id}").buildAndExpand(newDepoimento.getId()).toUri();
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
