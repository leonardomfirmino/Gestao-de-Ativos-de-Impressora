package br.com.api_imp.gestaoimp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.api_imp.gestaoimp.dto.ResponseUnidadeDTO;
import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.service.LocalService;


@RestController
@RequestMapping("/api/projects")
public class LocalController {
    @Autowired
    private LocalService localService;

    @GetMapping 
    public ResponseEntity<List<ResponseUnidadeDTO>> listarUnidades(){
        List<ResponseUnidadeDTO> unidades=localService.listarUnidade();
        return ResponseEntity.ok(unidades);
    }

    @PostMapping("/criarLocal")
    public LocalModel criarLocal(@RequestBody LocalModel local) {
        return localService.criarLocal(local);
    }
    @DeleteMapping("/deletarLocal/{id}")
    public void deletarLocal(@PathVariable Long id) {
        localService.deletarLocal(id);
    }  

}
