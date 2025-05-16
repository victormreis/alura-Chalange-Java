package com.alura.challange.controller;

import com.alura.challange.records.DestinosDTO;
import com.alura.challange.records.DestinosDTORequest;
import com.alura.challange.service.DestinoService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/destinos")
public class DestinosController {

    private final DestinoService destinoService;

    public DestinosController(DestinoService destinoService) {
        this.destinoService = destinoService;
    }

    @GetMapping
    public ResponseEntity getDestinos(@RequestParam(defaultValue = "") String nome) {
        var destinos = destinoService.getDestinos(nome);
        if (destinos.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensagem", "Nenhum destino foi encontrado"));
        }
        return ResponseEntity.ok(destinos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinosDTO> getDestinoById(@PathVariable Long id) {

        return ResponseEntity.ok(destinoService.getDestinoById(id));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<DestinosDTO> createDestino(
            @RequestParam(value = "foto1", required = false) MultipartFile foto1,
            @RequestParam(value = "foto2", required = false) MultipartFile foto2,
            @RequestParam(value = "nome", required = true) String nome,
            @RequestParam(value = "textoDescritivo", required = false) String textoDescritivo,
            UriComponentsBuilder uriBuilder) throws IOException {

        var dtoRequest = new DestinosDTORequest(foto1, foto2, nome, textoDescritivo);

        var destino = destinoService.createDestino(dtoRequest);

        var uri = uriBuilder.path("/destinos/{id}").buildAndExpand(destino.getId()).toUri();

        return ResponseEntity.created(uri).body(new DestinosDTO(destino));
    }

    @Transactional
    @PutMapping
    public ResponseEntity<DestinosDTO> updateDestino(@RequestBody DestinosDTO destinosDTOResponse) {

        var destino = destinoService.updateDestino(destinosDTOResponse);

        return ResponseEntity.ok(new DestinosDTO(destino));
    }

    @Transactional
    @DeleteMapping("/{id}")

    public ResponseEntity deleteDestino(@PathVariable Long id) {

        destinoService.deleteDestino(id);

        return ResponseEntity.noContent().build();
    }

}
