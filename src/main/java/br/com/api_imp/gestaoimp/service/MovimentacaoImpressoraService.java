package br.com.api_imp.gestaoimp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.el.stream.*;
import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.model.ImpressorasModel;
import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;
import br.com.api_imp.gestaoimp.repository.ImpressorasRepository;
import br.com.api_imp.gestaoimp.repository.LocalRepository;
import br.com.api_imp.gestaoimp.repository.MovimentacaoImpressoraRepository;

@Service
public class MovimentacaoImpressoraService {
    private final MovimentacaoImpressoraRepository movimentacaoImpressoraRepository;
    private final ImpressorasRepository impressorasRepository;
    private final LocalRepository localRepository;

    public MovimentacaoImpressoraService(MovimentacaoImpressoraRepository movimentacaoImpressoraRepository,
            ImpressorasRepository impressorasRepository, LocalRepository localRepository) {
        this.movimentacaoImpressoraRepository = movimentacaoImpressoraRepository;
        this.impressorasRepository = impressorasRepository;
        this.localRepository = localRepository;
    }

    public MovimentacaoImpressoraModel criarMovimentacao(MovimentacaoImpressoraModel novaMov) {

        String serial = novaMov.getImpressora().getSerial();
        String local = novaMov.getLocal().getNomeLocal();

        ImpressorasModel impressoraCadastrada = impressorasRepository.findBySerial(serial)
                .orElseThrow(() -> new RuntimeException("Serial não encontrado: " + serial));
        LocalModel localCadastrado = localRepository.findByNomeLocal(local)
                .orElseThrow(() -> new RuntimeException("Local não encontrado: " + local));

       
        novaMov.setImpressora(impressoraCadastrada);
        novaMov.setLocal(localCadastrado);

        
        Long impressoraId = novaMov.getImpressora().getId();

        if (novaMov.getDataInicio() == null) {
            novaMov.setDataInicio(LocalDateTime.now());
        }

        return movimentacaoImpressoraRepository.save(novaMov);
    }

    public MovimentacaoImpressoraModel atualizarMovimentacao(Long id,MovimentacaoImpressoraModel movimentacaoImpressoraModel) {
        MovimentacaoImpressoraModel movimentacaoImpressoraExistente = buscarMovimentacaoPorId(id);
        movimentacaoImpressoraExistente.setDataFim(movimentacaoImpressoraModel.getDataFim());
        movimentacaoImpressoraExistente.setDataInicio(movimentacaoImpressoraModel.getDataInicio());
        movimentacaoImpressoraExistente.setImpressora(movimentacaoImpressoraModel.getImpressora());
        movimentacaoImpressoraExistente.setLocal(movimentacaoImpressoraModel.getLocal());
        return movimentacaoImpressoraRepository.save(movimentacaoImpressoraExistente);
    }

    public void deletarMovimentacao(Long id) {
        movimentacaoImpressoraRepository.deleteById(id);
    }

    public List<MovimentacaoImpressoraModel> listarMovimentacoes() {
        return movimentacaoImpressoraRepository.findAll();
    }

    public MovimentacaoImpressoraModel buscarMovimentacaoPorId(Long id) {
        return movimentacaoImpressoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }

}
