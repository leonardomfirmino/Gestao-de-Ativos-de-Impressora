package br.com.api_imp.gestaoimp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.repository.LocalRepository;

@Service
public class LocalService {
    private final LocalRepository localRepository;
    
    public LocalService(LocalRepository localRepository){
        this.localRepository = localRepository;
    }

    public List<String> buscarUnidades(){
        return localRepository.buscarUnidades();
    }
    public List<String> buscarDepartamentos(){
        return localRepository.buscarDepartamentos();
    }
    public List<String> buscarLocais(){
        return localRepository.buscarLocais();
    }
    public LocalModel criarLocal(LocalModel local){
        return localRepository.save(local);
    }
    public LocalModel atualizarLocal(Long id, LocalModel local){
        LocalModel localExistente = buscarLocal(id);
        localExistente.setDepartamento(local.getDepartamento());
        localExistente.setNomeLocal(local.getNomeLocal());
        localExistente.setDepartamento(local.getDepartamento());
        localExistente.setUnidade(local.getUnidade());
        return localRepository.save(localExistente);
    }
    public void deletarLocal(Long id){
        localRepository.deleteById(id);
    }
    public LocalModel buscarLocal(Long id){
        return localRepository.findById(id).orElseThrow(() -> new RuntimeException("Local não encontrado"));
    }
    
}
