package br.com.api_imp.gestaoimp.service;

import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.model.HistoricoModel;
import br.com.api_imp.gestaoimp.repository.HistoricoRepository;


@Service
public class HistoricoService {
    private final HistoricoRepository historicoRepository;

    public HistoricoService(HistoricoRepository historicoRepository){
        this.historicoRepository = historicoRepository;
    }

    public Iterable<HistoricoModel> listarHistoricos(){
        return historicoRepository.findAll();
    }
    public HistoricoModel criarHistorico(HistoricoModel historicoModel){
        return historicoRepository.save(historicoModel);
    }

    public HistoricoModel atualizarHistorico(Long id, HistoricoModel historicoAtualizado){
       HistoricoModel historicoExistente = historicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Histórico não encontrado"));
        historicoExistente.setDataInicio(historicoAtualizado.getDataInicio());
        historicoExistente.setDataFim(historicoAtualizado.getDataFim());
        historicoExistente.setObservacao(historicoAtualizado.getObservacao());
        historicoExistente.setImpressora(historicoAtualizado.getImpressora());
        historicoExistente.setStatus(historicoAtualizado.getStatus());

        return historicoRepository.save(historicoExistente);
    }

    public void deletarHistorico(Long id){
        historicoRepository.deleteById(id);
    }
    
    public HistoricoModel buscarHistorico(Long id){
        return historicoRepository.findById(id).orElseThrow(() -> new RuntimeException("Histórico não encontrado"));
    }   

   
}
