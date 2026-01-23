package br.com.api_imp.gestaoimp.controller;
import org.springframework.web.bind.annotation.*;
import br.com.api_imp.gestaoimp.model.ImpressorasModel;
import br.com.api_imp.gestaoimp.service.ImpressorasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;






@RestController
@RequestMapping("/impressoras")
public class ImpressorasController {
    
    @Autowired
    private ImpressorasService impressorasService;
    
    

    @GetMapping
    public Iterable<ImpressorasModel> listarImpressoras(){
        return impressorasService.mostrarImp();      
    }

    @GetMapping("/buscar/{id}")
    public ImpressorasModel buscarImpressora(@PathVariable Long id){ 
        return impressorasService.buscarImp(id);
    }

    @PostMapping("/criarImp")
    public ImpressorasModel criarImpressora(@RequestBody ImpressorasModel impressoraAdd) { 
        return impressorasService.salvarImp(impressoraAdd);
    }

    @DeleteMapping("/deletar/{id}")
    public void deletarImpressora(@PathVariable Long id) {
        impressorasService.deletarImp(id);
    }
    @PutMapping("/atualizar/{id}")
    public ImpressorasModel atualizarImpressora(@PathVariable Long id, @RequestBody ImpressorasModel impressoraAtualizada) {
        return impressorasService.atualizarImp(id, impressoraAtualizada);
    }
    

}