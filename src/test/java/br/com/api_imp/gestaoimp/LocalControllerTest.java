package br.com.api_imp.gestaoimp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import br.com.api_imp.gestaoimp.controller.LocalController;
import br.com.api_imp.gestaoimp.model.HistoricoModel;
import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.service.LocalService;

@WebMvcTest(LocalController.class)
public class LocalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocalService localService;

    @Autowired
    private ObjectMapper objectMapper; 

    @Test
    void deveListarLocais() throws Exception {
        List<LocalModel> lista = new ArrayList<>();
        lista.add(new LocalModel());
        lista.add(new LocalModel());
        Mockito.when(localService.obterLocais())
               .thenReturn(lista);
               
        mockMvc.perform(get("/locais")).andExpect(status().isOk());
    }

    @Test
    void deveBuscarLocalPorId() throws Exception {
        LocalModel local = new LocalModel();
        Mockito.when(localService.buscarLocal(1L))
               .thenReturn(local);

        mockMvc.perform(get("/locais/local/1"))
               .andExpect(status().isOk());
    }
    
    @Test
    void deveCriarLocal() throws Exception {
        LocalModel local = new LocalModel();
        Mockito.when(localService.criarLocal(Mockito.any(LocalModel.class)))
               .thenReturn(local);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/locais/criarLocal")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(local)))
               .andExpect(status().isOk());
    }
    
    @Test
    void deveAtualizarLocal() throws Exception {
        LocalModel local = new LocalModel();
        Mockito.when(localService.atualizarLocal(Mockito.eq(1L), Mockito.any(LocalModel.class)))
               .thenReturn(local);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/locais/atualizarLocal/1")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(local)))
               .andExpect(status().isOk());
    }

    @Test
    void deveDeletarLocal() throws Exception {
        Mockito.doNothing().when(localService).deletarLocal(1L);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/locais/deletarLocal/1"))
               .andExpect(status().isOk());
    }

    @Test
    void deveRetornarErroQuandoLocalNaoExistir() throws Exception {
        Mockito.when(localService.buscarLocal(99L))
               .thenThrow(new RuntimeException("Local não encontrado"));

        mockMvc.perform(get("/locais/Buscar/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroQuandoAtualizarLocalInexistente() throws Exception {
        Mockito.when(localService.atualizarLocal(eq(99L), any(LocalModel.class)))
               .thenThrow(new RuntimeException("Local não encontrado"));
        mockMvc.perform(put("/locais/atualizarLocal/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LocalModel())))
               .andExpect(status().isNotFound());
    }

    
}
