package br.com.api_imp.gestaoimp.service;


import java.util.List;

import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.dto.ResponseMovimentacaoDTO;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;


import br.com.api_imp.gestaoimp.repository.MovimentacaoImpressoraRepository;


@Service
public class MovimentacaoImpressoraService {
    private final MovimentacaoImpressoraRepository movimentacaoImpressoraRepository;

    public MovimentacaoImpressoraService(MovimentacaoImpressoraRepository movimentacaoImpressoraRepository) {
        this.movimentacaoImpressoraRepository = movimentacaoImpressoraRepository;
       
    }

    public List<ResponseMovimentacaoDTO> listarPorUnidade(Long idUnidade) {
        List<MovimentacaoImpressoraModel> movimentacoes = movimentacaoImpressoraRepository.findByUnidadeId(idUnidade);
        
        return movimentacoes.stream()
                .map(ResponseMovimentacaoDTO::fromModel)
                .toList();
    }
    
    public void deletarMovimentacao(Long id) {
        movimentacaoImpressoraRepository.deleteById(id);
    }

}
