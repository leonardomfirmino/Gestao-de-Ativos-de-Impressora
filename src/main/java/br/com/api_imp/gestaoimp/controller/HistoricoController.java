package br.com.api_imp.gestaoimp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.api_imp.gestaoimp.model.HistoricoModel;
import br.com.api_imp.gestaoimp.service.HistoricoService;


@RestController
@RequestMapping("/historico")
public class HistoricoController {
    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    public Iterable<HistoricoModel> listarHistoricos(){
        return historicoService.listarHistoricos();
    }
    @PostMapping("/Cadastrar")
    public HistoricoModel criarHistorico(@RequestBody HistoricoModel historicoModel){
        return historicoService.criarHistorico(historicoModel);
    }
    @DeleteMapping("/Deletar/{id}")
    public void deletarHistorico(@PathVariable Long id){
        historicoService.deletarHistorico(id);
    }
    @PutMapping("/Atualizar/{id}")
    public HistoricoModel atualizarHistorico(@PathVariable Long id, @RequestBody HistoricoModel historicoModel){
        return historicoService.atualizarHistorico(id, historicoModel); 
    }
    @GetMapping("/Buscar/{id}")
    public HistoricoModel buscarHistorico(@PathVariable Long id){
        return historicoService.buscarHistorico(id);
    }
}
    
