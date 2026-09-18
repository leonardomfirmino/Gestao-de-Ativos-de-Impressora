package br.com.api_imp.gestaoimp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.api_imp.gestaoimp.dto.AlocacaoDTO;
import br.com.api_imp.gestaoimp.dto.TrocaDTO;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;
import br.com.api_imp.gestaoimp.service.MovimentacaoImpressoraService;


@RestController
@RequestMapping("/movimentacoesImpressora")
public class MovimentacaoImpressoraController {
    @Autowired
    private MovimentacaoImpressoraService movimentacaoImpressoraService;   

    @GetMapping
    public List<MovimentacaoImpressoraModel> getMovimentacoesImpressora() {
        return movimentacaoImpressoraService.listarMovimentacoes();
    }
    
    @PostMapping("/criarMovimentacaoImpressora")
    public MovimentacaoImpressoraModel criarMovimentacaoImpressora( @RequestBody MovimentacaoImpressoraModel movimentacaoImpressora) {
        return movimentacaoImpressoraService.criarMovimentacao(movimentacaoImpressora);
    }

    @PostMapping("/cadastrarImpressoraComLocal")
    public MovimentacaoImpressoraModel cadastrarImpressoraComLocal(@RequestBody MovimentacaoImpressoraModel cadastrarImpressoraComLocal){
        return movimentacaoImpressoraService.cadastrarImpressoraComLocal(cadastrarImpressoraComLocal);
    }

    @PostMapping("/alocar")
    public ResponseEntity<Void> alocarImpressora(@RequestBody AlocacaoDTO dto) {
        movimentacaoImpressoraService.alocarImpressoraDoEstoque(dto);
        return ResponseEntity.ok().build();
    }

    
    @PostMapping("/trocar")
    public ResponseEntity<Void> trocarImpressora(@RequestBody TrocaDTO dto) {
        movimentacaoImpressoraService.realizarTrocaTecnica(dto);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/deletarMovimentacaoImpressora/{id}")
    public void deletarMovimentacaoImpressora(@PathVariable Long id) {
        movimentacaoImpressoraService.deletarMovimentacao(id);
    }
    @GetMapping("/buscarMovimentacaoImpressora/{id}")
    public MovimentacaoImpressoraModel getMovimentacaoImpressoraById(@PathVariable Long id) {
        return movimentacaoImpressoraService.buscarMovimentacaoPorId(id);
    }  
}
