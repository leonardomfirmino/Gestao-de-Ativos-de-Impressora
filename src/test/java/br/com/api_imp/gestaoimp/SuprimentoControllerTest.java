package br.com.api_imp.gestaoimp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.ArrayList;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.api_imp.gestaoimp.controller.SuprimentoController;
import br.com.api_imp.gestaoimp.model.SuprimentoModel;
import br.com.api_imp.gestaoimp.service.SuprimentoService;

@WebMvcTest(SuprimentoController.class)
class SuprimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SuprimentoService suprimentoService;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void deveListarSuprimentos_comSucesso() throws Exception {
        List<SuprimentoModel> lista = new ArrayList<>();
        lista.add(new SuprimentoModel());
        lista.add(new SuprimentoModel());

        Mockito.when(suprimentoService.listarSuprimentos())
               .thenReturn(lista);
       mockMvc.perform(get("/suprimento"))
              .andExpect(status().isOk());
    }

    @Test
    void deveCriarSuprimento_comSucesso() throws Exception {
        SuprimentoModel suprimento = new SuprimentoModel();

        Mockito.when(suprimentoService.criarSuprimento(any(SuprimentoModel.class)))
               .thenReturn(suprimento);

        mockMvc.perform(post("/suprimento/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suprimento)))
               .andExpect(status().isOk());
    }

    @Test
    void deveBuscarSuprimentoPorId_comSucesso() throws Exception {
        Mockito.when(suprimentoService.buscarSuprimento(1L))
               .thenReturn(new SuprimentoModel());

        mockMvc.perform(get("/suprimento/buscar/1"))
               .andExpect(status().isOk());
    }

    @Test
    void deveAtualizarSuprimento_comSucesso() throws Exception {
        Mockito.when(suprimentoService.atualizarSuprimento(eq(1L), any(SuprimentoModel.class)))
               .thenReturn(new SuprimentoModel());

        mockMvc.perform(put("/suprimento/atualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SuprimentoModel())))
               .andExpect(status().isOk());
    }

    @Test
    void deveDeletarSuprimento_comSucesso() throws Exception {
        Mockito.doNothing().when(suprimentoService).deletarSuprimento(1L);

        mockMvc.perform(delete("/suprimento/deletar/1"))
               .andExpect(status().isOk());
    }

    

    @Test
    void deveRetornarErroQuandoSuprimentoNaoExistir() throws Exception {
        Mockito.when(suprimentoService.buscarSuprimento(99L))
               .thenThrow(new RuntimeException("Suprimento não encontrado"));

        mockMvc.perform(get("/suprimento/buscar/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarAtualizarSuprimentoInexistente() throws Exception {
        Mockito.when(suprimentoService.atualizarSuprimento(eq(99L), any(SuprimentoModel.class)))
               .thenThrow(new RuntimeException("suprimento não encontrado"));

        mockMvc.perform(put("/suprimento/atualizar/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SuprimentoModel())))
               .andExpect(status().isNotFound());
    }
}

