package br.com.api_imp.gestaoimp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.api_imp.gestaoimp.dto.RequestImpressoraDTO;
import br.com.api_imp.gestaoimp.dto.ResponseImpressorasDTO;
import br.com.api_imp.gestaoimp.dto.InversaoImpressorasDTO;
import br.com.api_imp.gestaoimp.service.ImpressorasService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("projects")
public class ImpressorasController {

    @Autowired
    private ImpressorasService impressorasService;

    @GetMapping("{id_unidade}/impressora")
    public ResponseEntity<List<ResponseImpressorasDTO>> getImpressoras(@PathVariable  Long id_unidade) {   
        return  ResponseEntity.ok(impressorasService.listarImpressoras(id_unidade));   
    }

    @PostMapping
    public ResponseEntity<Void> criarImpressora(@RequestBody RequestImpressoraDTO iDto) {
        impressorasService.cadastrar(iDto);
        return  ResponseEntity.ok().build();
    }

    @PostMapping("/movimentar")
    public ResponseEntity<Void> trocarImpressora(@RequestBody RequestImpressoraDTO iDto) {
        impressorasService.movimentar(iDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/inverter")
    public ResponseEntity<Void> inverterImpressoras(@RequestBody InversaoImpressorasDTO dto) {
        impressorasService.inverter(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPlanilha(@RequestParam("file") MultipartFile file) {

        try {
            impressorasService.processarPlanilha(file);
            return ResponseEntity.ok("Importado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    public void deletarImpressora(@PathVariable Long id) {
        impressorasService.deletarImp(id);
    }


}
