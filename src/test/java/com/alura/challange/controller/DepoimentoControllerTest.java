package com.alura.challange.controller;

import com.alura.challange.model.Depoimento;
import com.alura.challange.records.DepoimentosDTO;
import com.alura.challange.service.DepoimentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@WebMvcTest(DepoimentoController.class)
class DepoimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepoimentoService depoimentoService;

    @Autowired
    private JacksonTester<DepoimentosDTO> jacksonTester;


    @Test
    @DisplayName("Should return status code 200 when called")
    void getDepoimentos() throws Exception {
        var depoimentoDto = new DepoimentosDTO(null, "test", "Depoimento", "Autor");

        when(depoimentoService.listarDepoimentos()).thenReturn(List.of(depoimentoDto));

        mockMvc.perform(get("/depoimentos")) // Fazer a requisição GET
                .andExpect(status().isOk()) // Validar que o status é 200
                .andExpect(jsonPath("$[0].autor").value("Autor")) // Validar o autor do primeiro item
                .andExpect(jsonPath("$[0].depoimento").value("Depoimento")); // Validar o conteúdo do primeiro item
    }


    @Test
    @DisplayName("Should Return status code 200 when find a testimony by a valid id")
    void getDepoimentoById() throws Exception {
        var depoimentoDto = new DepoimentosDTO(1L, "test", "Depoimento", "Autor");

        when(depoimentoService.getDepoimentoById(1l)).thenReturn(depoimentoDto);

        mockMvc.perform(get("/depoimentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    @DisplayName("Should return status code")
    void updateDepoimento() throws Exception {
        var depoimentoDto = new DepoimentosDTO(1l, "test", "Depoimento", "Autor");

        var depoimento = new Depoimento(1L, "Depoimento", "Autor", "test att");


        when(depoimentoService.updateDepoimento(depoimentoDto)).thenReturn(depoimento);

        mockMvc.perform(put("/depoimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(depoimentoDto).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.foto").value("test att"));

    }

    @Test
    @DisplayName("Should return status code 200 when a testimony was created")
    void createDepoimento() throws Exception {
        var depoimentoDto = new DepoimentosDTO(1l, "test", "Depoimento", "Autor");
        var depoimento = new Depoimento(1L, "Depoimento", "Autor", "test att");

        doNothing().when(depoimentoService).createDepoimento(any(Depoimento.class));

        mockMvc.perform(post("/depoimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonTester.write(depoimentoDto).getJson()))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return status code 204 when a testimony was deleted")
    void deleteDepoimento() throws Exception {

        doNothing().when(depoimentoService).deleteDepoimento(1l);

        mockMvc.perform(delete("/depoimentos/{id}", 1l)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("Should return status code 200 with a list of testimony when called")
    void getRandomDepoimentos() throws Exception {
        var depoimento = new Depoimento(1l, "Depoimento", "Autor", "foto");

        when(depoimentoService.getRandomDepoimento()).thenReturn(List.of(depoimento));

        mockMvc.perform(get("/depoimentos/home").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].autor").value("Autor"));;
    }
}