package br.com.api_imp.gestaoimp.service;

import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;
import br.com.api_imp.gestaoimp.repository.MovimentacaoImpressoraRepository;

public class MovimentacaoImpressoraService {
    private final MovimentacaoImpressoraRepository movimentacaoImpressoraRepository;
    public MovimentacaoImpressoraService(MovimentacaoImpressoraRepository movimentacaoImpressoraRepository) {
        this.movimentacaoImpressoraRepository = movimentacaoImpressoraRepository;
    }
    public MovimentacaoImpressoraModel criarMovimentacao(MovimentacaoImpressoraModel movimentacaoImpressoraModel) {
        return movimentacaoImpressoraRepository.save(movimentacaoImpressoraModel);
    }
    public MovimentacaoImpressoraModel atualizarMovimentacao(Long id, MovimentacaoImpressoraModel movimentacaoImpressoraModel) {
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
    public Iterable<MovimentacaoImpressoraModel> listarMovimentacoes() {
        return movimentacaoImpressoraRepository.findAll();
    }
    public MovimentacaoImpressoraModel buscarMovimentacaoPorId(Long id) {
        return movimentacaoImpressoraRepository.findById(id).orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }
}
