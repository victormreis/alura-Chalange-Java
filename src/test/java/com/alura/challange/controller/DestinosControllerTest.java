package com.alura.challange.controller;

import com.alura.challange.config.errorHandling.ErrorHandlingValidation;
import com.alura.challange.model.Destino;
import com.alura.challange.records.DestinosDTO;
import com.alura.challange.records.DestinosDTORequest;
import com.alura.challange.service.DestinoService;
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
        var destinoDTO = new DestinosDTO(1L, "FotoAtualizada1", "FotoAtualizada2", "Destino Atualizado",  "Texto atualizado");
        var destino = new Destino(destinoDTO);

        when(destinoService.updateDestino(destinoDTO)).thenReturn(destino);

        mockMvc.perform(put("/destinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(destinoDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1l))
                .andExpect(jsonPath("$.foto1").value("FotoAtualizada1"))
                .andExpect(jsonPath("$.foto2").value("FotoAtualizada2"))
                .andExpect(jsonPath("$.nome").value("Destino Atualizado"))
                .andExpect(jsonPath("$.textoDescritivo").value("Texto atualizado"));



    }

    @Test
    @DisplayName("Should return status code 400 when invalid id was informed")
    void updateDestinoFail() throws Exception {
        var destinoDTO = new DestinosDTO(1l, "Foto","Foto",  "nome",  "texto Descritivo");

        when(destinoService.updateDestino(destinoDTO)).thenThrow(new ErrorHandlingValidation("Invalid ID!"));

        mockMvc.perform(put("/destinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(destinoDTO).getJson()))
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