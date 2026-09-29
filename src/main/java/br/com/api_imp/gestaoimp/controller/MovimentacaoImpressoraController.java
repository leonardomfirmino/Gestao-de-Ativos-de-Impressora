package br.com.api_imp.gestaoimp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.api_imp.gestaoimp.dto.ResponseMovimentacaoDTO;
import br.com.api_imp.gestaoimp.service.MovimentacaoImpressoraService;

@RestController
@RequestMapping("/api/projects")
public class MovimentacaoImpressoraController {
    @Autowired
    private MovimentacaoImpressoraService movimentacaoImpressoraService;

    @GetMapping("/{id_unidade}/movimentacoesImpressora")
    public ResponseEntity<List<ResponseMovimentacaoDTO>> getMovimentacoesPorUnidade(
            @PathVariable("id_unidade") Long idUnidade) {
        List<ResponseMovimentacaoDTO> lista = movimentacaoImpressoraService.listarPorUnidade(idUnidade);
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/deletarMovimentacaoImpressora/{id}")
    public void deletarMovimentacaoImpressora(@PathVariable Long id) {
        movimentacaoImpressoraService.deletarMovimentacao(id);
    }
}
