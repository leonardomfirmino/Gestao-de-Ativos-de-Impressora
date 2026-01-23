package br.com.api_imp.gestaoimp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.service.LocalService;

@RestController
@RequestMapping("/locais")
public class LocalController {
    @Autowired
    private LocalService localsService;
    
    @GetMapping
    public Iterable<LocalModel> getLocais() {
        return localsService.obterLocais();
    }

    @PostMapping("/criarLocal")
    public LocalModel criarLocal(@RequestBody LocalModel local) {
        return localsService.criarLocal(local);
    }
    @PutMapping("/atualizarLocal/{id}")
    public LocalModel atualizarLocal(@PathVariable Long id, @RequestBody LocalModel local) {
        return localsService.atualizarLocal(id, local);
    }
    @DeleteMapping("/deletarLocal/{id}")
    public void deletarLocal(@PathVariable Long id) {
        localsService.deletarLocal(id);
    }  
    @GetMapping("/local/{id}")
    public LocalModel buscarLocal(@PathVariable Long id) {
        return localsService.buscarLocal(id);
    }

}
