package com.iago.flowlog.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FocusControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnEmptyDiagnosticsWhenNoRecordsExist() throws Exception {
        mockMvc.perform(get("/diagnostico-produtividade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mediaNivelFoco").value(0.0))
                .andExpect(jsonPath("$.tempoTotalMinutos").value(0))
                .andExpect(jsonPath("$.totalRegistros").value(0));
    }

    @Test
    void shouldCreateRecordAndReturnDiagnosis() throws Exception {
        String request = "{" +
                "\"nivelFoco\": 5," +
                "\"tempoMinutos\": 45," +
                "\"comentario\": \"Concentração total em tarefa de desenvolvimento\"," +
                "\"categoria\": \"coding\"}";

        mockMvc.perform(post("/registro-foco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nivelFoco").value(5))
                .andExpect(jsonPath("$.tempoMinutos").value(45));

        mockMvc.perform(get("/diagnostico-produtividade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mediaNivelFoco").value(5.0))
                .andExpect(jsonPath("$.tempoTotalMinutos").value(45))
                .andExpect(jsonPath("$.totalRegistros").value(1));
    }

    @Test
    void shouldReturnBadRequestForInvalidFocusLevel() throws Exception {
        String request = "{" +
                "\"nivelFoco\": 10," +
                "\"tempoMinutos\": 20," +
                "\"comentario\": \"Teste invalido\"}";

        mockMvc.perform(post("/registro-foco")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }
}
