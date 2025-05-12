package com.alura.challange.controller;

import com.alura.challange.records.DestinosDTO;
import com.alura.challange.service.DestinoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
