package com.alura.challange.controller;

import com.alura.challange.config.errorHandling.ErrorHandlingValidation;
import com.alura.challange.model.Destino;
import com.alura.challange.records.DestinosDTO;
import com.alura.challange.records.DestinosDTORequest;
import com.alura.challange.records.DestinosUpdateRequest;
import com.alura.challange.service.DestinoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
@WebMvcTest(DestinosController.class)
class DestinosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DestinoService destinoService;

    @Autowired
    private JacksonTester<DestinosDTO> jacksonTester;

    @Test
    @DisplayName("Should Return status code 200 when called")
    void getDestinos() throws Exception {
        var destino = new DestinosDTO(1l, "Foto","Foto", "nome",  "Texto descritivo");

        when(destinoService.getDestinos("")).thenReturn(List.of(destino));

        mockMvc.perform(get("/destinos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

    }


    @Test
    @DisplayName("Should Return status code 200 when id is found")
    void getDestinoById() throws Exception {
        var destino = new DestinosDTO(1l, "Foto","Foto", "nome", "Texto descritivo");

        when(destinoService.getDestinoById(1l)).thenReturn((destino));

        mockMvc.perform(get("/destinos/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Should Return status code 400 when id is not found")
    void getDestinoByIdFail() throws Exception {
        var destino = new DestinosDTO(1l, "Foto","Foto", "nome",  "Texto descritivo");

        when(destinoService.getDestinoById(10L))
                .thenThrow(new ErrorHandlingValidation("Id not found!"));

        mockMvc.perform(get("/destinos/{id}", 10l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return Status code 201 when a new destination was created")
    void createDestino() throws Exception {
        MockMultipartFile foto1 = new MockMultipartFile("foto1", "foto1.png", "image/png", "fake-image-1".getBytes());
        MockMultipartFile foto2 = new MockMultipartFile("foto2", "foto2.png", "image/png", "fake-image-2".getBytes());

        var destino = new Destino();
        destino.setId(1L);
        destino.setNome("Destino Teste");

        when(destinoService.createDestino(any(DestinosDTORequest.class))).thenReturn(destino);

        mockMvc.perform(multipart("/destinos")
                .file(foto1)
                .file(foto2)
                .param("nome", "Destino teste")
                .param("textoDescritivo", "Descricao teste" )
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return Status code 400 when invalid request was inserted")
    void createDestinoFail() throws Exception {

        mockMvc.perform(multipart("/destinos")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status code 200 when a destiny was updated")
    void updateDestino() throws Exception {
        MockMultipartFile foto1 = new MockMultipartFile("foto1", "foto1.jpg", "image/jpeg", "dummy image 1".getBytes());
        MockMultipartFile foto2 = new MockMultipartFile("foto2", "foto2.jpg", "image/jpeg", "dummy image 2".getBytes());

        String nome = "Destino Atualizado";
        String textoDescritivo = "Texto atualizado";
        String id = "1";

        DestinosDTO destinosDTO = new DestinosDTO(1l, "url_foto1", "url_foto2", nome, textoDescritivo);

        Destino destino = new Destino(destinosDTO);

        when(destinoService.updateDestino(any(DestinosUpdateRequest.class))).thenReturn(destino);

        mockMvc.perform(multipart(HttpMethod.PUT, "/destinos")
                        .file(foto1)
                        .file(foto2)
                        .param("id", id)
                        .param("nome", nome)
                        .param("textoDescritivo", textoDescritivo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.foto1").value("url_foto1"))
                .andExpect(jsonPath("$.foto2").value("url_foto2"))
                .andExpect(jsonPath("$.nome").value(nome))
                .andExpect(jsonPath("$.textoDescritivo").value(textoDescritivo));
    }

    @Test
    @DisplayName("Should return status code 400 when invalid id was informed")
    void updateDestinoFail() throws Exception {
        MockMultipartFile foto1 = new MockMultipartFile("foto1", "foto1.jpg", "image/jpeg", "dummy image 1".getBytes());
        MockMultipartFile foto2 = new MockMultipartFile("foto2", "foto2.jpg", "image/jpeg", "dummy image 2".getBytes());

        when(destinoService.updateDestino(any(DestinosUpdateRequest.class)))
                .thenThrow(new ErrorHandlingValidation("Invalid ID!"));

        mockMvc.perform(multipart(HttpMethod.PUT, "/destinos")
                        .file(foto1)
                        .file(foto2)
                        .param("id", "9999")
                        .param("nome", "Teste inválido")
                        .param("textoDescritivo", "Desc inválida"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status code 200 when a destiny was deleted")
    void deleteDestino() throws Exception {

        doNothing().when(destinoService).deleteDestino(1l);

        mockMvc.perform(delete("/destinos/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }
}