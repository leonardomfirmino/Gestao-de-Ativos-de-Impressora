package br.com.api_imp.gestaoimp.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.api_imp.gestaoimp.repository.ImpressorasRepository;
import br.com.api_imp.gestaoimp.repository.MovimentacaoImpressoraRepository;
import br.com.api_imp.gestaoimp.model.ImpressorasModel;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;
import br.com.api_imp.gestaoimp.model.StatusImpressoras;

@Service
public class ImpressorasService {
    private final ImpressorasRepository impressorasRepository;
    private final LocalService localService;
    private final MovimentacaoImpressoraRepository movimentacaoRepository;

    private String getCellValue(Row row, int index) {// Obter valor de célula considerando diferentes tipos de dados
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

    public List<String> buscarModelos() {
        return impressorasRepository.buscarModelos();
    }

    public List<String> buscarSeriais() {
        return impressorasRepository.buscarSerial();
    }

    public ImpressorasService(ImpressorasRepository impressorasRepository, LocalService localService,
            MovimentacaoImpressoraRepository movimentacaoRepository) {
        this.impressorasRepository = impressorasRepository;
        this.localService = localService;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public void salvarOuAtualizarComMovimentacao(// Salvar ou atualizar impressora e registrar movimentação
            String modelo,
            String serial,
            String nomeLocal,
            String unidade,
            String status,
            String filaImpressao,
            String ip) {

        ImpressorasModel impressora = impressorasRepository
                .findBySerialAndIp(serial, ip)
                .orElse(new ImpressorasModel());

        impressora.setModelo(modelo);
        impressora.setSerial(serial);
        impressora.setStatusAtual(StatusImpressoras.valueOf(status));
        impressora.setFilaImpressao(filaImpressao);
        impressora.setIp(ip);

        impressora = impressorasRepository.save(impressora);

        var local = localService.buscarOuCriarLocal(nomeLocal, unidade);

        var movAtual = movimentacaoRepository.buscarMovimentacaoAtiva(impressora.getId());

        if (movAtual.isPresent()) {
            var mov = movAtual.get();

            if (mov.getLocal().getIdLocal() == local.getIdLocal()) {
                return;
            }

            mov.setDataFim(java.time.LocalDateTime.now());
            movimentacaoRepository.save(mov);
        }

        MovimentacaoImpressoraModel novaMov = new MovimentacaoImpressoraModel();

        novaMov.setImpressora(impressora);
        novaMov.setLocal(local);
        novaMov.setDataInicio(java.time.LocalDateTime.now());
        novaMov.setDataFim(null);

        movimentacaoRepository.save(novaMov);
    }

    public void processarPlanilha(MultipartFile file) throws Exception {// Processar planilha Excel para importar dados
                                                                        // de impressoras
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue;

            String modelo = getCellValue(row, 0);
            String serial = getCellValue(row, 1);
            String status = getCellValue(row, 2);
            String filaImpressao = getCellValue(row, 3);
            String ip = getCellValue(row, 4);
            String nomeLocal = getCellValue(row, 5);
            String unidade = getCellValue(row, 6);

            if (impressorasRepository.existsBySerial(serial)) {
                throw new RuntimeException("Já existe uma impressora com esse serial: " + serial);
            }

            salvarOuAtualizarComMovimentacao(modelo,
                    serial,
                    nomeLocal,
                    unidade,
                    status,
                    filaImpressao,
                    ip);
        }

        workbook.close();
    }

    public ImpressorasModel atualizarImp(Long id, ImpressorasModel impressorasModel) {// Atualizar impressora por ID
        ImpressorasModel impressoraExistente = impressorasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impressora não encontrada"));
        impressoraExistente.setStatusAtual(impressorasModel.getStatusAtual());
        impressoraExistente.setModelo(impressorasModel.getModelo());
        impressoraExistente.setSerial(impressorasModel.getSerial());
        impressoraExistente.setFilaImpressao(impressorasModel.getFilaImpressao());
        return impressorasRepository.save(impressoraExistente);
    }

    public void deletarImp(Long id) {// Deletar impressora por ID
        impressorasRepository.deleteById(id);
    }

}
