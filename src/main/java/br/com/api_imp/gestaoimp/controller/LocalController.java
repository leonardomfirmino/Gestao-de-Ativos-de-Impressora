package br.com.api_imp.gestaoimp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.service.LocalService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/locais")
public class LocalController {
    @Autowired
    private LocalService localsService;
    
   @GetMapping("/unidades")
    public List<String> unidades(){
        return localsService.buscarUnidades();
    }

    @GetMapping("/departamentos")
    public List<String> departamentos(){
        return localsService.buscarDepartamentos();
    }

    @GetMapping("/nomes")
    public List<String> nomes(){
        return localsService.buscarLocais();
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
