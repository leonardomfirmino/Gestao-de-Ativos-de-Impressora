package br.com.api_imp.gestaoimp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.api_imp.gestaoimp.controller.HistoricoController;
import br.com.api_imp.gestaoimp.model.HistoricoModel;
import br.com.api_imp.gestaoimp.service.HistoricoService;

@WebMvcTest(HistoricoController.class)
class HistoricoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoricoService historicoService;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void deveListarHistoricos_comSucesso() throws Exception {
        List<HistoricoModel> lista = new ArrayList<>();
        lista.add(new HistoricoModel());
        lista.add(new HistoricoModel());

        Mockito.when(historicoService.listarHistoricos())
               .thenReturn(lista);

        mockMvc.perform(get("/historico"))
               .andExpect(status().isOk());
    }

    @Test
    void deveCriarHistorico_comSucesso() throws Exception {
        HistoricoModel historico = new HistoricoModel();

        Mockito.when(historicoService.criarHistorico(any(HistoricoModel.class)))
               .thenReturn(historico);

        mockMvc.perform(post("/historico/Cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(historico)))
               .andExpect(status().isOk());
    }

    @Test
    void deveBuscarHistoricoPorId_comSucesso() throws Exception {
        Mockito.when(historicoService.buscarHistorico(1L))
               .thenReturn(new HistoricoModel());

        mockMvc.perform(get("/historico/Buscar/1"))
               .andExpect(status().isOk());
    }

    @Test
    void deveAtualizarHistorico_comSucesso() throws Exception {
        Mockito.when(historicoService.atualizarHistorico(eq(1L), any(HistoricoModel.class)))
               .thenReturn(new HistoricoModel());

        mockMvc.perform(put("/historico/Atualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new HistoricoModel())))
               .andExpect(status().isOk());
    }

    @Test
    void deveDeletarHistorico_comSucesso() throws Exception {
        Mockito.doNothing().when(historicoService).deletarHistorico(1L);

        mockMvc.perform(delete("/historico/Deletar/1"))
               .andExpect(status().isOk());
    }

    

    @Test
    void deveRetornarErroQuandoHistoricoNaoExistir() throws Exception {
        Mockito.when(historicoService.buscarHistorico(99L))
               .thenThrow(new RuntimeException("Histórico não encontrado"));

        mockMvc.perform(get("/historico/Buscar/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarAtualizarHistoricoInexistente() throws Exception {
        Mockito.when(historicoService.atualizarHistorico(eq(99L), any(HistoricoModel.class)))
               .thenThrow(new RuntimeException("Histórico não encontrado"));

        mockMvc.perform(put("/historico/Atualizar/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new HistoricoModel())))
               .andExpect(status().isNotFound());
    }
}

