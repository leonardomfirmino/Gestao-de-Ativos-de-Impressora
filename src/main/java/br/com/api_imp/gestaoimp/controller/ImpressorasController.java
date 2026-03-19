package br.com.api_imp.gestaoimp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.api_imp.gestaoimp.model.ImpressorasModel;
import br.com.api_imp.gestaoimp.service.ImpressorasService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/impressoras")
public class ImpressorasController {

    @Autowired
    private ImpressorasService impressorasService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPlanilha(@RequestParam("file") MultipartFile file) {

        try {
            impressorasService.processarPlanilha(file);
            return ResponseEntity.ok("Importado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/modelo")
    public List<String> modelo() {
        return impressorasService.buscarModelos();
    }
    
    @GetMapping("/serial")
    public List<String> serial() {
        return impressorasService.buscarSeriais();
    }

    @DeleteMapping("/deletar/{id}")
    public void deletarImpressora(@PathVariable Long id) {
        impressorasService.deletarImp(id);
    }

    @PutMapping("/atualizar/{id}")
    public ImpressorasModel atualizarImpressora(@PathVariable Long id,
            @RequestBody ImpressorasModel impressoraAtualizada) {
        return impressorasService.atualizarImp(id, impressoraAtualizada);
    }

}