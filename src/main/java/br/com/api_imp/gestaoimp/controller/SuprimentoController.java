package br.com.api_imp.gestaoimp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import br.com.api_imp.gestaoimp.model.SuprimentoModel;
import br.com.api_imp.gestaoimp.service.SuprimentoService;


@RestController
@RequestMapping("/suprimento")
public class SuprimentoController {
    @Autowired
    private SuprimentoService suprimentoService;

    @GetMapping
    public Iterable<SuprimentoModel> getSuprimentos(SuprimentoModel suprimentoModel) {
        return suprimentoService.listarSuprimentos();
    }
    @PostMapping("/criar")
    public SuprimentoModel criarSuprimento(@RequestBody SuprimentoModel suprimento) {
        return suprimentoService.criarSuprimento(suprimento);
    }
    @PutMapping("atualizar/{id}")
    public SuprimentoModel atualizarSuprimento(@PathVariable Long id, @RequestBody SuprimentoModel suprimento) {
        return suprimentoService.atualizarSuprimento(id, suprimento);
    }
    @DeleteMapping("deletar/{id}")
    public void deletarSuprimento(@PathVariable Long id) {
        suprimentoService.deletarSuprimento(id);
    }
    @GetMapping("buscar/{id}")
    public SuprimentoModel buscarSuprimento(@PathVariable Long id) {
        return suprimentoService.buscarSuprimento(id);
    }
}