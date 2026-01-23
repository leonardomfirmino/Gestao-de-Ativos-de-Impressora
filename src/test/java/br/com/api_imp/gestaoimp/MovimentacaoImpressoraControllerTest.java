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

import br.com.api_imp.gestaoimp.controller.MovimentacaoImpressoraController;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;
import br.com.api_imp.gestaoimp.service.MovimentacaoImpressoraService;

@WebMvcTest(MovimentacaoImpressoraController.class)
class MovimentacaoImpressoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimentacaoImpressoraService movimentacaoImpressoraService;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void deveListarMovimentacoes_comSucesso() throws Exception {
        List<MovimentacaoImpressoraModel> lista = new ArrayList<>();
        lista.add(new MovimentacaoImpressoraModel());
        lista.add(new MovimentacaoImpressoraModel());

        Mockito.when(movimentacaoImpressoraService.listarMovimentacoes())
               .thenReturn(lista);

        mockMvc.perform(get("/movimentacoesImpressora"))
               .andExpect(status().isOk());
    }

    @Test
    void deveCriarMovimentacaoImpressora_comSucesso() throws Exception {
        MovimentacaoImpressoraModel movimentacaoImpressora = new MovimentacaoImpressoraModel();

        Mockito.when(movimentacaoImpressoraService.criarMovimentacao(any(MovimentacaoImpressoraModel.class)))
               .thenReturn(movimentacaoImpressora);

        mockMvc.perform(post("/movimentacoesImpressora/criarMovimentacaoImpressora")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movimentacaoImpressora)))
               .andExpect(status().isOk());
    }

    @Test
    void deveBuscarMovimentacaoPorId_comSucesso() throws Exception {
        Mockito.when(movimentacaoImpressoraService.buscarMovimentacaoPorId(1L))
               .thenReturn(new MovimentacaoImpressoraModel());
        mockMvc.perform(get("/movimentacoesImpressora/buscarMovimentacaoImpressora/1"))
               .andExpect(status().isOk());
    }

    @Test
    void deveAtualizarMovimentacao_comSucesso() throws Exception {
        Mockito.when(movimentacaoImpressoraService.atualizarMovimentacao(eq(1L), any(MovimentacaoImpressoraModel.class)))
               .thenReturn(new MovimentacaoImpressoraModel());

        mockMvc.perform(put("/movimentacoesImpressora/atualizarMovimentacaoImpressora/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MovimentacaoImpressoraModel())))
               .andExpect(status().isOk());
    }

    @Test
    void deveDeletarMovimentacao_comSucesso() throws Exception {
        Mockito.doNothing().when(movimentacaoImpressoraService).deletarMovimentacao(1L);

        mockMvc.perform(delete("/movimentacoesImpressora/deletarMovimentacaoImpressora/1"))
               .andExpect(status().isOk());
    }

    

    @Test
    void deveRetornarErroQuandoMovimentacaoNaoExistir() throws Exception {
        Mockito.when(movimentacaoImpressoraService.buscarMovimentacaoPorId(99L))
               .thenThrow(new RuntimeException("Movimentação não encontrada"));

        mockMvc.perform(get("/movimentacoesImpressora/buscarMovimentacaoImpressora/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarAtualizarMovimentacaoInexistente() throws Exception {
        Mockito.when(movimentacaoImpressoraService.atualizarMovimentacao(eq(99L), any(MovimentacaoImpressoraModel.class)))
               .thenThrow(new RuntimeException("Movimentação não encontrada"));
        mockMvc.perform(put("/movimentacoesImpressora/atualizarMovimentacaoImpressora/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new MovimentacaoImpressoraModel())))
               .andExpect(status().isNotFound());
    }
}

