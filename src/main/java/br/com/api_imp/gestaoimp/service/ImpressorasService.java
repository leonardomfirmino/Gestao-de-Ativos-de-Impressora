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
import br.com.api_imp.gestaoimp.repository.LocalRepository;
import br.com.api_imp.gestaoimp.repository.MovimentacaoImpressoraRepository;
import br.com.api_imp.gestaoimp.model.ImpressorasModel;
import br.com.api_imp.gestaoimp.model.LocalModel;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;
import br.com.api_imp.gestaoimp.model.StatusImpressoras;

@Service
public class ImpressorasService {
    private final ImpressorasRepository impressorasRepository;
    private final LocalRepository localRepository;
    private final MovimentacaoImpressoraRepository movimentacaoRepository;

   

    public ImpressorasService(ImpressorasRepository impressorasRepository, LocalRepository localRepository,
            MovimentacaoImpressoraRepository movimentacaoRepository) {
        this.impressorasRepository = impressorasRepository;
        this.localRepository = localRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

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


    public void processarPlanilha(MultipartFile file) throws Exception {
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
            }else{
            
                ImpressorasModel impNova = new ImpressorasModel();
                impNova.setSerial(serial);
                impNova.setModelo(modelo);
                impNova.setFilaImpressao(filaImpressao);
                impNova.setStatus(StatusImpressoras.valueOf(status));
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

    public ImpressorasModel atualizarImp(Long id, ImpressorasModel impressorasModel) {// Atualizar impressora por ID
        ImpressorasModel impressoraExistente = impressorasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impressora não encontrada"));
        impressoraExistente.setStatusAtual(impressorasModel.getStatusAtual());
        impressoraExistente.setModelo(impressorasModel.getModelo());
        impressoraExistente.setSerial(impressorasModel.getSerial());
        impressoraExistente.setFilaImpressao(impressorasModel.getFilaImpressao());
        return impressorasRepository.save(impressoraExistente);
    }

    public List<ImpressorasModel> listarImpressoras() {
        return impressorasRepository.findAll();
    }
    
    public ImpressorasModel cadastrarImpressora(ImpressorasModel iModel){
        return impressorasRepository.save(iModel);
    }
    
    public void deletarImp(Long id) {
        impressorasRepository.deleteById(id);
    }

}
