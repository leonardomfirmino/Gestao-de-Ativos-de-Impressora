package br.com.api_imp.gestaoimp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;
import br.com.api_imp.gestaoimp.service.MovimentacaoImpressoraService;

@RestController
@RequestMapping("/movimentacoesImpressora")
public class MovimentacaoImpressoraController {
    @Autowired
    private MovimentacaoImpressoraService movimentacaoImpressoraService;   

    @GetMapping
    public Iterable<MovimentacaoImpressoraModel> getMovimentacoesImpressora() {
        return movimentacaoImpressoraService.listarMovimentacoes();
    }
    @PostMapping("/criarMovimentacaoImpressora")
    public MovimentacaoImpressoraModel criarMovimentacaoImpressora(@RequestBody MovimentacaoImpressoraModel movimentacaoImpressora) {
        return movimentacaoImpressoraService.criarMovimentacao(movimentacaoImpressora);
    }
    @PutMapping("/atualizarMovimentacaoImpressora/{id}")
    public MovimentacaoImpressoraModel atualizarMovimentacaoImpressora(@PathVariable Long id, @RequestBody MovimentacaoImpressoraModel movimentacaoImpressora) {
        return movimentacaoImpressoraService.atualizarMovimentacao(id, movimentacaoImpressora);
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
