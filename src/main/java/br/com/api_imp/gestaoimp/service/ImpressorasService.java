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

@Service
public class ImpressorasService {
    private final ImpressorasRepository impressorasRepository;
    private final LocalService localService;
    private final MovimentacaoImpressoraRepository movimentacaoRepository;
    private String getCellValue(Row row, int index) {// Obter valor de célula considerando diferentes tipos de dados
        Cell cell = row.getCell(index);

        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
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
            String status,
            String nomeLocal,
            String departamento,
            String unidade) {

        ImpressorasModel impressora = impressorasRepository
                .findBySerial(serial)
                .orElse(new ImpressorasModel());

        impressora.setModelo(modelo);
        impressora.setSerial(serial);

        impressora = impressorasRepository.save(impressora);

        var local = localService.buscarOuCriarLocal(nomeLocal, departamento, unidade);

        var movAtual = movimentacaoRepository.buscarMovimentacaoAtiva(impressora.getId());

        if (movAtual.isPresent()) {
            var mov = movAtual.get();

            if (mov.getLocal().equals(local.getIdLocal())) {
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
    
    public void processarPlanilha(MultipartFile file) throws Exception {// Processar planilha Excel para importar dados de impressoras
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

    

        for (Row row : sheet) {
            if (row.getRowNum() == 0)
                continue;

            String modelo = getCellValue(row, 0);
            String serial = getCellValue(row, 1);
            String departamento = getCellValue(row, 2);
            String nomeLocal = getCellValue(row, 3);
            String unidade = getCellValue(row, 4);
            String status = getCellValue(row, 5);

            salvarOuAtualizarComMovimentacao(
                    modelo,
                    serial,
                    status,
                    nomeLocal,
                    departamento,
                    unidade);
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

    public ImpressorasModel buscarImp(Long id) {// Buscar impressora por ID
        return impressorasRepository.findById(id).orElseThrow(() -> new RuntimeException("Impressora não encontrada"));
    }

    public List<ImpressorasModel> mostrarImp() {// Listar todas as impressoras
        return impressorasRepository.findAll();
    }

    public void deletarImp(Long id) {// Deletar impressora por ID
        impressorasRepository.deleteById(id);
    }

}
