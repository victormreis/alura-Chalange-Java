package com.alura.challange.controller;

import com.alura.challange.model.Depoimento;
import com.alura.challange.records.DepoimentosDTO;
import com.alura.challange.service.DepoimentoService;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/depoimentos")
public class DepoimentoController {

    @Autowired
    private DepoimentoService depoimentoService;

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


    @PutMapping
    @Transactional
    public ResponseEntity updateDepoimento(@RequestBody DepoimentosDTO depoimento) {
        var depoimentoUpdated = depoimentoService.updateDepoimento(depoimento);
        return ResponseEntity.ok(new DepoimentosDTO(depoimentoUpdated));
    }

    @PostMapping
    @Transactional
    public ResponseEntity createDepoimento(@RequestBody DepoimentosDTO depoimento, UriComponentsBuilder uriComponentsBuilder) {
        var newDepoimento = new Depoimento(depoimento);

        var uri = uriComponentsBuilder.path("/depoimentos/{id}").buildAndExpand(newDepoimento.getId()).toUri();
        depoimentoService.createDepoimento(newDepoimento);
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
