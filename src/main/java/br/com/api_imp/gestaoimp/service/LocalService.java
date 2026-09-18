package br.com.api_imp.gestaoimp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.repository.LocalRepository;

@Service
public class LocalService {
    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public List<String> buscarUnidades() {
        return localRepository.buscarUnidades();
    }

    public List<String> buscarLocais() {
        return localRepository.buscarLocais();
    }

    public LocalModel criarLocal(LocalModel local) {
        return localRepository.save(local);
    }

    public void deletarLocal(Long id) {
        localRepository.deleteById(id);
    }
}
