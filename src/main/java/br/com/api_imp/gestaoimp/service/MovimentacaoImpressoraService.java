package br.com.api_imp.gestaoimp.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.api_imp.gestaoimp.dto.AlocacaoDTO;
import br.com.api_imp.gestaoimp.dto.TrocaDTO;
import br.com.api_imp.gestaoimp.model.ImpressorasModel;
import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;
import br.com.api_imp.gestaoimp.model.StatusImpressoras;
import br.com.api_imp.gestaoimp.repository.ImpressorasRepository;
import br.com.api_imp.gestaoimp.repository.LocalRepository;
import br.com.api_imp.gestaoimp.repository.MovimentacaoImpressoraRepository;
import jakarta.transaction.Transactional;

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

        if (novaMov.getDataFim() == null) {
            List<String> movimentacoesAtivas = movimentacaoImpressoraRepository
                    .findByDataFimIsNull();

            if (movimentacoesAtivas.contains(novaMov.getImpressora().getSerial())) {
                throw new RuntimeException(
                        "A impressora já possui uma movimentação ativa. Finalize a movimentação atual antes de criar uma nova.");
            }

        }

        if (novaMov.getDataInicio() == null) {
            novaMov.setDataInicio(LocalDateTime.now());
        }

        return movimentacaoImpressoraRepository.save(novaMov);
    }

    public MovimentacaoImpressoraModel cadastrarImpressoraComLocal(MovimentacaoImpressoraModel novImpLocal) {
        
        ImpressorasModel novaImpressora = novImpLocal.getImpressora();
        LocalModel novoLocal = novImpLocal.getLocal();
        boolean verificacaoImpressoraExistente = impressorasRepository.existsBySerial(novaImpressora.getSerial());

        
        if (!verificacaoImpressoraExistente) {
            if (novaImpressora != null) {
                novaImpressora = impressorasRepository.save(novaImpressora);
            }
        }else{
            throw new RuntimeException("ImpressoraModel  já existe no sistema");
        }

        if (novoLocal != null) {
            novoLocal = localRepository.save(novoLocal);
        } else {
            throw new RuntimeException("Dados do local são obrigatórios.");
        }

        novImpLocal.setImpressora(novaImpressora);
        novImpLocal.setLocal(novoLocal);

        if (novImpLocal.getDataInicio() == null) {
            novImpLocal.setDataInicio(LocalDateTime.now());
        }

        return movimentacaoImpressoraRepository.save(novImpLocal);
    }

    public void deletarMovimentacao(Long id) {
        movimentacaoImpressoraRepository.deleteById(id);
    }

    public List<MovimentacaoImpressoraModel> listarMovimentacoes() {
        
        return movimentacaoImpressoraRepository.findAllComDetalhes();
    }

    public MovimentacaoImpressoraModel buscarMovimentacaoPorId(Long id) {
        return movimentacaoImpressoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
    }

    @Transactional
    public void alocarImpressoraDoEstoque(AlocacaoDTO dto) {
        ImpressorasModel  imp = impressorasRepository.findById(dto.idImp())
                .orElseThrow(() -> new RuntimeException("ImpressoraModel  não encontrada"));
        LocalModel local = localRepository.findById(dto.idLocal())
                .orElseThrow(() -> new RuntimeException("Local não encontrado"));

        
        imp.setStatusAtual(StatusImpressoras.Alocado);
        impressorasRepository.save(imp);

        
        MovimentacaoImpressoraModel  mov = new MovimentacaoImpressoraModel();
        mov.setImpressora(imp);
        mov.setLocal(local);
        mov.setDataInicio(LocalDateTime.now());
        mov.setDescricao(dto.descricao());
        movimentacaoImpressoraRepository.save(mov);
    }

    @Transactional
    public void realizarTrocaTecnica(TrocaDTO dto) {
       
        MovimentacaoImpressoraModel  movAtual = movimentacaoImpressoraRepository.buscarMovimentacaoAtiva(dto.idImpAtiva())
                .orElseThrow(() -> new RuntimeException("Movimentação ativa não encontrada"));

        LocalDateTime agora = LocalDateTime.now();
        movAtual.setDataFim(agora);
        movimentacaoImpressoraRepository.save(movAtual);

        
        ImpressorasModel  impAntiga = movAtual.getImpressora();
        impAntiga.setStatusAtual(StatusImpressoras.TrocaTecnica);
        impressorasRepository.save(impAntiga);

        
        ImpressorasModel  impBackup = impressorasRepository.findById(dto.idImpBackup())
                .orElseThrow(() -> new RuntimeException("ImpressoraModel  de backup não encontrada"));
        impBackup.setStatusAtual(StatusImpressoras.Alocado);
        impressorasRepository.save(impBackup);

        MovimentacaoImpressoraModel  novaMov = new MovimentacaoImpressoraModel();
        novaMov.setImpressora(impBackup);
        novaMov.setLocal(movAtual.getLocal());
        novaMov.setDataInicio(agora);
        novaMov.setDescricao("Troca técnica: Substituiu serial " + impAntiga.getSerial() + ". " + dto.descricao());
        movimentacaoImpressoraRepository.save(novaMov);
    }


}
