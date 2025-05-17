package com.alura.challange.controller;

import com.alura.challange.model.Depoimento;
import com.alura.challange.model.Destino;
import com.alura.challange.records.DepoimentosDTO;
import com.alura.challange.records.DepoimentosRequestDTO;
import com.alura.challange.records.DepoimentosUpdateDTO;
import com.alura.challange.service.DepoimentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

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
        var depoimentoDto = new DepoimentosDTO(null, "test", "Depoimento", "Autor", 1l);

        when(depoimentoService.listarDepoimentos()).thenReturn(List.of(depoimentoDto));

        mockMvc.perform(get("/depoimentos")) // Fazer a requisição GET
                .andExpect(status().isOk()) // Validar que o status é 200
                .andExpect(jsonPath("$[0].autor").value("Autor")) // Validar o autor do primeiro item
                .andExpect(jsonPath("$[0].depoimento").value("Depoimento")); // Validar o conteúdo do primeiro item
    }


    @Test
    @DisplayName("Should Return status code 200 when find a testimony by a valid id")
    void getDepoimentoById() throws Exception {
        var depoimentoDto = new DepoimentosDTO(1L, "test", "Depoimento", "Autor", 1l);

        when(depoimentoService.getDepoimentoById(1l)).thenReturn(depoimentoDto);

        mockMvc.perform(get("/depoimentos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    @DisplayName("Should return status code")
    void updateDepoimento() throws Exception {
        var depoimentoDto = new DepoimentosDTO(1l, "test", "Depoimento", "Autor", 1l);

        var destino = new Destino();
        destino.setId(1l);

        var fotoMock = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE,
                "fake image content".getBytes());

        var depoimento = new Depoimento(1L, "Depoimento", "Autor", "test att", destino);


        when(depoimentoService.updateDepoimento(any(DepoimentosUpdateDTO.class))).thenReturn(depoimento);

        mockMvc.perform(multipart("/depoimentos")
                .file(fotoMock)
                .param("id", "1")
                .param("depoimento", "Depoimento")
                .param("autor", "Autor")
                .param("destinoId", "1")
                        .with(request -> {request.setMethod("PUT"); return request;}))
                .andExpect(status().isOk());


    }

    @Test
    @DisplayName("Should return status code 201 when a testimony was created")
    void createDepoimento() throws Exception {
        var destino = new Destino();
        destino.setId(1l);

        var fotoMock = new MockMultipartFile("foto", "foto.jpg", MediaType.IMAGE_JPEG_VALUE,
                "fake image content".getBytes());

        var depoimento = new Depoimento(1L, "Depoimento", "Autor", "test att", destino);

        when(depoimentoService.createDepoimento(any(DepoimentosRequestDTO.class))).thenReturn(depoimento);

        mockMvc.perform(multipart("/depoimentos")
                        .file(fotoMock)
                        .param("depoimento", "Depoimento")
                        .param("autor", "Autor")
                        .param("destinoId", "1"))
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
        var destino = new Destino();
        destino.setId(1l);

        var depoimento = new Depoimento(1l, "Depoimento", "Autor", "foto", destino);

        when(depoimentoService.getRandomDepoimento()).thenReturn(List.of(depoimento));

        mockMvc.perform(get("/depoimentos/home").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].autor").value("Autor"));
        ;
    }
}