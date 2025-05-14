package com.alura.challange.controller;

import com.alura.challange.config.errorHandling.ErrorHandlingValidation;
import com.alura.challange.model.Destino;
import com.alura.challange.records.DestinosDTO;
import com.alura.challange.service.DestinoService;
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
        var destino = new DestinosDTO(1l, "Foto", "nome", 599.97);

        when(destinoService.getDestinos("")).thenReturn(List.of(destino));

        mockMvc.perform(get("/destinos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

    }


    @Test
    @DisplayName("Should Return status code 200 when id is found")
    void getDestinoById() throws Exception {
        var destino = new DestinosDTO(1l, "Foto", "nome", 599.97);

        when(destinoService.getDestinoById(1l)).thenReturn((destino));

        mockMvc.perform(get("/destinos/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Should Return status code 400 when id is not found")
    void getDestinoByIdFail() throws Exception {
        var destino = new DestinosDTO(1l, "Foto", "nome", 599.97);

        when(destinoService.getDestinoById(10L))
                .thenThrow(new ErrorHandlingValidation("Id not found!"));

        mockMvc.perform(get("/destinos/{id}", 10l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return Status code 200 when a new destiny was created")
    void createDestino() throws Exception {
        var destinoDTO = new DestinosDTO(1l, "Foto", "nome", 599.97);
        var destino = new Destino(1l, "Foto", "nome", 599.97, true);

        doNothing().when(destinoService).createDestino(any(Destino.class));

        mockMvc.perform(post("/destinos")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(destinoDTO).getJson()))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Should return Status code 400 when invalid json was inserted")
    void createDestinoFail() throws Exception {


        doNothing().when(destinoService).createDestino(any(Destino.class));

        mockMvc.perform(post("/destinos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Should return status code 200 when a destiny was updated")
    void updateDestino() throws Exception {
        var destinoDTO = new DestinosDTO(1l, "Foto", "nome", 599.97);
        var destino = new Destino(1l, "Foto att", "nome att", 699.97, true);

        when(destinoService.updateDestino(destinoDTO)).thenReturn(destino);

        mockMvc.perform(put("/destinos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonTester.write(destinoDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.foto").value("Foto att"));



    }

    @Test
    @DisplayName("Should return status code 400 when invalid id was informed")
    void updateDestinoFail() throws Exception {
        var destinoDTO = new DestinosDTO(1l, "Foto", "nome", 599.97);

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