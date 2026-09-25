package br.com.api_imp.gestaoimp.service;


import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.dto.ResponseUnidadeDTO;
import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.repository.LocalRepository;
import br.com.api_imp.gestaoimp.interfaces.*;


@Service
public class LocalService {
    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public LocalModel criarLocal(LocalModel local) {
        return localRepository.findByNomeLocalAndUnidade(local.getNomeLocal(), local.getUnidade())
                .orElseGet(() -> localRepository.save(local));
    }

   public List<ResponseUnidadeDTO> listarUnidade() {
    List<UnidadeProjecao> lista = localRepository.findUnidadesComTotalImpressoras();
    
    if (lista == null || lista.isEmpty()) {
        return Collections.emptyList(); // Evita corpo nulo
    }

    return lista.stream()
        .map(p -> new ResponseUnidadeDTO(
            p.getId(), 
            p.getName(), 
            p.getDescription(), 
            p.getPrinterCount()
        ))
        .toList();
}

    public void deletarLocal(Long id) {
        localRepository.deleteById(id);
    }
}
