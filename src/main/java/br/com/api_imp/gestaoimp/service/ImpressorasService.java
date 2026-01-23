package br.com.api_imp.gestaoimp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.repository.ImpressorasRepository;
import br.com.api_imp.gestaoimp.model.ImpressorasModel;



@Service
public class ImpressorasService {
    private final ImpressorasRepository impressorasRepository;

    public ImpressorasService(ImpressorasRepository impressorasRepository){
        this.impressorasRepository = impressorasRepository;
    }

    public ImpressorasModel atualizarImp(Long id, ImpressorasModel impressorasModel){
        ImpressorasModel impressoraExistente =  impressorasRepository.findById(id).orElseThrow(() -> new RuntimeException("Impressora não encontrada"));
        impressoraExistente.setStatusAtual(impressorasModel.getStatusAtual());
        impressoraExistente.setModelo(impressorasModel.getModelo());
        impressoraExistente.setSerial(impressorasModel.getSerial());
        impressoraExistente.setFilaImpressao(impressorasModel.getFilaImpressao());
        return impressorasRepository.save(impressoraExistente);
    }
    public ImpressorasModel buscarImp(Long id){
        return impressorasRepository.findById(id).orElseThrow(() -> new RuntimeException("Impressora não encontrada"));
    }
    public List<ImpressorasModel> mostrarImp(){
        return impressorasRepository.findAll();
    }

    public ImpressorasModel salvarImp(ImpressorasModel impressorasModel){
        return impressorasRepository.save(impressorasModel);
    }

    public void deletarImp(Long id){
        impressorasRepository.deleteById(id);
    }

}
