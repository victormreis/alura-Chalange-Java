package com.alura.challange.controller;

import com.alura.challange.model.Destino;
import com.alura.challange.records.DestinosDTO;
import com.alura.challange.service.DestinoService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/destinos")
public class DestinosController {

    private final DestinoService destinoService;

    public DestinosController(DestinoService destinoService) {
        this.destinoService = destinoService;
    }

    @GetMapping
    public ResponseEntity<List<DestinosDTO>> getDestinos() {

        return ResponseEntity.ok(destinoService.getDestinos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinosDTO> getDestinoById(@PathVariable Long id) {

        return ResponseEntity.ok(destinoService.getDestinoById(id));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<DestinosDTO> createDestino(@RequestBody DestinosDTO destinoDTO, UriComponentsBuilder uriBuilder) {

        var destino = new Destino(destinoDTO);

        destinoService.createDestino(destino);

        var uri = uriBuilder.path("/destinos/{id}").buildAndExpand(destino.getId()).toUri();

        return ResponseEntity.created(uri).body(new DestinosDTO(destino));
    }

    @Transactional
    @PutMapping
    public ResponseEntity<DestinosDTO> updateDestino(@RequestBody DestinosDTO destinosDTO) {

        var destino = destinoService.updateDestino(destinosDTO);

        return ResponseEntity.ok(new DestinosDTO(destino));
    }

    @Transactional
    @DeleteMapping("/{id}")

    public ResponseEntity deleteDestino(@PathVariable Long id) {

         destinoService.deleteDestino(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<DestinosDTO>> SearchDestinosByName(@RequestParam(defaultValue = "") String nome) {

        System.out.println("Chamou" + nome);

        var destinos = destinoService.getDestinosByName(nome);

        return ResponseEntity.ok(destinos);
    }
}
