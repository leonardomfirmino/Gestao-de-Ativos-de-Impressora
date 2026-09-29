package br.com.api_imp.gestaoimp.controller;


import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardViewController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "http://localhost:8080/api"; // URL do seu Back-end atual

    // Helper para simular ou injetar o Token JWT obtido no login (/api/auth/login)
    private String obterTokenJwt() {
        return "Bearer SEU_TOKEN_AQUI_DEPOIS_DO_LOGIN";
    }

    // 1. Renderiza o esqueleto inicial com a lista de projetos (GET /api/projects)
    @GetMapping
    public String renderDashboard(Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", obterTokenJwt());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            // Consome sua API Rest atual de Projetos
            ResponseEntity<Object[]> response = restTemplate.exchange(API_URL + "/projects", HttpMethod.GET, entity, Object[].class);
            model.addAttribute("projetos", response.getBody());
        } catch (Exception e) {
            model.addAttribute("projetos", new Object[]{}); // Fallback em caso de erro
        }

        return "dashboard";
    }

    // 2. Criação de Local via HTMX (POST /api/projects/criarLocal)
    @PostMapping("/projects/criarLocal")
    public String criarLocal(@RequestParam String unidade, @RequestParam String nomeLocal, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", obterTokenJwt());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("unidade", unidade, "nomeLocal", nomeLocal);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // Dispara contra a sua API
        restTemplate.postForEntity(API_URL + "/projects/criarLocal", entity, String.class);

        // Busca a lista atualizada para re-renderizar o fragmento na tela
        ResponseEntity<Object[]> response = restTemplate.exchange(API_URL + "/projects", HttpMethod.GET, new HttpEntity<>(headers), Object[].class);
        model.addAttribute("projetos", response.getBody());

        return "dashboard :: fragmentoLocais"; // HTMX atualiza apenas a grid de cards
    }

    // 3. Busca impressoras de uma unidade específica (GET /api/projects/{id_unidade}/impressora)
    @GetMapping("/projects/{idUnidade}/impressoras")
    public String buscarImpressorasUnidade(@PathVariable Long idUnidade, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", obterTokenJwt());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Consome seu endpoint existente de buscar por unidade
        ResponseEntity<Object[]> response = restTemplate.exchange(API_URL + "/projects/" + idUnidade + "/impressora", HttpMethod.GET, entity, Object[].class);
        
        model.addAttribute("impressoras", response.getBody());
        model.addAttribute("unidadeNome", "ID #" + idUnidade);

        return "dashboard :: fragmentoImpressoras"; // HTMX injeta a tabela no container inferior
    }

    // 4. Deleta uma impressora via HTMX (DELETE /api/projects/deletar/{id})
    @DeleteMapping("/projects/deletar/{id}")
    @ResponseBody
    public String deletarImpressora(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", obterTokenJwt());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(API_URL + "/projects/deletar/" + id, HttpMethod.DELETE, entity, Void.class);

        // Retorna uma string vazia para o HTMX remover a linha da tabela instantaneamente
        return ""; 
    }
}
