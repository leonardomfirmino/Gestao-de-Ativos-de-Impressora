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

import br.com.api_imp.gestaoimp.controller.ImpressorasController;
import br.com.api_imp.gestaoimp.model.ImpressorasModel;
import br.com.api_imp.gestaoimp.service.ImpressorasService;

@WebMvcTest(ImpressorasController.class)
public class ImpressorasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImpressorasService impressorasService;

    @Autowired
    private ObjectMapper objectMapper;

    
    @Test
    void deveListarImpressoras() throws Exception {
        List<ImpressorasModel> lista = new ArrayList<>();
        lista.add(new ImpressorasModel());
        lista.add(new ImpressorasModel());

        Mockito.when(impressorasService.mostrarImp())
               .thenReturn(lista);

        mockMvc.perform(get("/impressoras"))
               .andExpect(status().isOk());
    }

    
    @Test
    void deveBuscarImpressoraPorId() throws Exception {
        ImpressorasModel impressora = new ImpressorasModel();

        Mockito.when(impressorasService.buscarImp(1L))
               .thenReturn(impressora);

        mockMvc.perform(get("/impressoras/buscar/1"))
               .andExpect(status().isOk());
    }

   
    @Test
    void deveCriarImpressora() throws Exception {
        ImpressorasModel impressora = new ImpressorasModel();

        Mockito.when(impressorasService.salvarImp(any(ImpressorasModel.class)))
               .thenReturn(impressora);

        mockMvc.perform(post("/impressoras/criarImp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(impressora)))
               .andExpect(status().isOk());
    }

    
    @Test
    void deveAtualizarImpressora() throws Exception {
        ImpressorasModel impressora = new ImpressorasModel();

        Mockito.when(impressorasService.atualizarImp(eq(1L), any(ImpressorasModel.class)))
               .thenReturn(impressora);

        mockMvc.perform(put("/impressoras/atualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(impressora)))
               .andExpect(status().isOk());
    }

    
    @Test
    void deveDeletarImpressora() throws Exception {
        Mockito.doNothing().when(impressorasService).deletarImp(1L);

        mockMvc.perform(delete("/impressoras/deletar/1"))
               .andExpect(status().isOk());
    }

    @Test
    void deveRetornarErroQuandoImpressoraNaoExistir() throws Exception {
        Mockito.when(impressorasService.buscarImp(99L))
               .thenThrow(new RuntimeException("impressora não encontrada"));

        mockMvc.perform(get("/impressoras/Buscar/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroQuandoAtualizarImpressoraInexistente() throws Exception {
        Mockito.when(impressorasService.atualizarImp(eq(99L), any(ImpressorasModel.class)))
               .thenThrow(new RuntimeException("impressora não encontrada"));
        mockMvc.perform(put("/impressoras/atualizar/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ImpressorasModel())))
               .andExpect(status().isNotFound());
    }
}

