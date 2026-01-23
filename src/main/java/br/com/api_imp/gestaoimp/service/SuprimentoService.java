package br.com.api_imp.gestaoimp.service;

import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.model.SuprimentoModel;
import br.com.api_imp.gestaoimp.repository.SuprimentoRepository;

@Service
public class SuprimentoService {
    private final SuprimentoRepository suprimentoRepository;
    public SuprimentoService(SuprimentoRepository suprimentoRepository){
        this.suprimentoRepository = suprimentoRepository;
    }
    public SuprimentoModel criarSuprimento(SuprimentoModel suprimento){
        return suprimentoRepository.save(suprimento);
    }
    public Iterable<SuprimentoModel> listarSuprimentos(){
        return suprimentoRepository.findAll();
    }
    public SuprimentoModel atualizarSuprimento(Long id, SuprimentoModel suprimento){
        SuprimentoModel suprimentoExistente = buscarSuprimento(id);
        suprimentoExistente.setImpressora(suprimento.getImpressora());
        suprimentoExistente.setNivelSuprimento(suprimento.getNivelSuprimento());
        suprimentoExistente.setPrevisaoTermino(suprimento.getPrevisaoTermino());
        suprimentoExistente.setTipoSuprimento(suprimento.getTipoSuprimento());
        suprimentoExistente.setUltimaAtualizacao(suprimento.getUltimaAtualizacao());
        return suprimentoRepository.save(suprimentoExistente);
    }
    public void deletarSuprimento(Long id){
        suprimentoRepository.deleteById(id);
    }
    public SuprimentoModel buscarSuprimento(Long id){
        return suprimentoRepository.findById(id).orElseThrow(() -> new RuntimeException("Suprimento não encontrado"));
    }
    
    
    
}
