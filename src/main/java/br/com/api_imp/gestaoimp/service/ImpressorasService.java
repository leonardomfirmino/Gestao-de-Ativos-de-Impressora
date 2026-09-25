package br.com.api_imp.gestaoimp.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.api_imp.gestaoimp.repository.ImpressorasRepository;
import br.com.api_imp.gestaoimp.repository.LocalRepository;
import br.com.api_imp.gestaoimp.repository.MovimentacaoImpressoraRepository;
import jakarta.transaction.Transactional;
import br.com.api_imp.gestaoimp.dto.RequestImpressoraDTO;
import br.com.api_imp.gestaoimp.dto.ResponseImpressorasDTO;
import br.com.api_imp.gestaoimp.dto.InversaoImpressorasDTO;
import br.com.api_imp.gestaoimp.model.ImpressorasModel;
import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;
import br.com.api_imp.gestaoimp.model.StatusImpressoras;

@Service
public class ImpressorasService {
    private final ImpressorasRepository impressorasRepository;
    private final LocalRepository localRepository;
    private final MovimentacaoImpressoraRepository movimentacaoRepository;

    public ImpressorasService(ImpressorasRepository impressorasRepository, LocalRepository localRepository,MovimentacaoImpressoraRepository movimentacaoRepository) {
        this.impressorasRepository = impressorasRepository;
        this.localRepository = localRepository;
        this.movimentacaoRepository=movimentacaoRepository;
    }

    private String getCellValue(Row row, int index) {
        Cell cell = row.getCell(index);

        if (cell == null)
            return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

    public void processarPlanilha(MultipartFile file) throws Exception {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue;

            String modelo = getCellValue(row, 0);
            String serial = getCellValue(row, 1);
            String status = getCellValue(row, 2);
            String assetTag = getCellValue(row, 3);
            String ip = getCellValue(row, 4);
            String nomeLocal = getCellValue(row, 5);
            String unidade = getCellValue(row, 6);

            if (impressorasRepository.existsBySerial(serial)) {
                throw new RuntimeException("Já existe uma impressora com esse serial: " + serial);
            }else{
            
                ImpressorasModel impNova = new ImpressorasModel();
                impNova.setSerial(serial);
                impNova.setModelo(modelo);
                impNova.setAssetTag(assetTag);
                impNova.setStatusAtual(StatusImpressoras.valueOf(status));
                impNova.setIp(ip);
                impressorasRepository.save(impNova);
                LocalModel locNovo= new  LocalModel();
                locNovo.setNomeLocal(nomeLocal);
                locNovo.setUnidade(unidade);
                localRepository.findByNomeLocalAndUnidade(nomeLocal, unidade).orElseGet(()->localRepository.save(locNovo));
           
            }
        }
        workbook.close();
    }

    public List<ResponseImpressorasDTO> listarImpressoras(Long id_unidade ) {
        List<ResponseImpressorasDTO> impressoras = impressorasRepository.findAllWithImpressoraUnidade(id_unidade).stream().map(ResponseImpressorasDTO::new).toList();
        return  impressoras;
    }
    
    @Transactional
    public void cadastrar(RequestImpressoraDTO dto) {
        ImpressorasModel novaImpressora = new ImpressorasModel();
        novaImpressora.setModelo(dto.model());
        novaImpressora.setSerial(dto.serial());
        novaImpressora.setAssetTag(dto.assetTag());
        novaImpressora.setIp(dto.ip());
        novaImpressora.setStatusAtual(StatusImpressoras.valueOf(dto.status()));

        if (dto.local() != null) {
            LocalModel localInicial = localRepository.findById(Long.parseLong(dto.local()))
                .orElseThrow(() -> new RuntimeException("Local não encontrado"));
            novaImpressora.setLocalAtual(localInicial);
        }

       
        impressorasRepository.save(novaImpressora);
    }
    
    @Transactional
    public void movimentar(RequestImpressoraDTO dto) {
        
        ImpressorasModel impressora = impressorasRepository.findSerial(dto.serial())
            .orElseThrow(() -> new RuntimeException("Impressora não encontrada"));

        LocalModel localAntigo = impressora.getLocalAtual();
        LocalModel novoLocal = localRepository.findById(Long.parseLong(dto.local()))
            .orElseThrow(() -> new RuntimeException("Local de destino não encontrado"));

        
        StatusImpressoras novoStatus = StatusImpressoras.valueOf(dto.status());
        validarTransicao(impressora.getStatusAtual(), novoStatus);
        impressora.setLocalAtual(novoLocal);
        impressora.setStatusAtual(novoStatus);
        impressorasRepository.save(impressora);

        
        MovimentacaoImpressoraModel historico = new MovimentacaoImpressoraModel();
        historico.setImpressora(impressora);
        historico.setLocalOrigem(localAntigo);
        historico.setLocalDestino(novoLocal);
        historico.setDataMovimentacao(LocalDateTime.now());
        movimentacaoRepository.save(historico);
    }

    private void validarTransicao(StatusImpressoras atual, StatusImpressoras destino) {
        boolean permitida = switch (atual) {
            case ATIVA -> destino == StatusImpressoras.BACKUP || destino == StatusImpressoras.ESTOQUE
                    || destino == StatusImpressoras.MANUTENCAO;
            case BACKUP -> destino == StatusImpressoras.ESTOQUE;
            case ESTOQUE -> destino == StatusImpressoras.ATIVA || destino == StatusImpressoras.MANUTENCAO
                    || destino == StatusImpressoras.DESATIVADA || destino == StatusImpressoras.BACKUP;
            case MANUTENCAO -> destino == StatusImpressoras.ESTOQUE || destino == StatusImpressoras.BACKUP;
            case DESATIVADA -> false;
        };
        if (!permitida) throw new IllegalArgumentException("Esta transição de status não é permitida.");
    }

    @Transactional
    public void inverter(InversaoImpressorasDTO dto) {
        if (dto == null || dto.sourcePrinterId() == null || dto.targetPrinterId() == null
                || dto.sourcePrinterId().equals(dto.targetPrinterId())) {
            throw new IllegalArgumentException("Informe duas impressoras diferentes para a inversão.");
        }
        ImpressorasModel ativa = impressorasRepository.findById(dto.sourcePrinterId())
                .orElseThrow(() -> new IllegalArgumentException("Impressora ativa não encontrada."));
        ImpressorasModel backup = impressorasRepository.findById(dto.targetPrinterId())
                .orElseThrow(() -> new IllegalArgumentException("Impressora de backup não encontrada."));
        if (ativa.getStatusAtual() != StatusImpressoras.ATIVA || backup.getStatusAtual() != StatusImpressoras.BACKUP) {
            throw new IllegalArgumentException("Selecione uma impressora ativa e uma impressora de backup.");
        }
        if (ativa.getLocalAtual() == null || backup.getLocalAtual() == null) {
            throw new IllegalArgumentException("As duas impressoras precisam possuir um local para a inversão.");
        }

        LocalModel localAtiva = ativa.getLocalAtual();
        LocalModel localBackup = backup.getLocalAtual();
        ativa.setLocalAtual(localBackup);
        ativa.setStatusAtual(StatusImpressoras.BACKUP);
        backup.setLocalAtual(localAtiva);
        backup.setStatusAtual(StatusImpressoras.ATIVA);
        impressorasRepository.save(ativa);
        impressorasRepository.save(backup);

        salvarHistorico(ativa, localAtiva, localBackup, "Trocada por impressora de backup");
        salvarHistorico(backup, localBackup, localAtiva, "Assumiu o lugar da impressora ativa");
    }

    private void salvarHistorico(ImpressorasModel impressora, LocalModel origem, LocalModel destino, String motivo) {
        MovimentacaoImpressoraModel historico = new MovimentacaoImpressoraModel();
        historico.setImpressora(impressora);
        historico.setLocalOrigem(origem);
        historico.setLocalDestino(destino);
        historico.setDataMovimentacao(LocalDateTime.now());
        historico.setDescricaoMotivo(motivo);
        movimentacaoRepository.save(historico);
    }
    
    public void deletarImp(Long id) {
        impressorasRepository.deleteById(id);
    }

}
