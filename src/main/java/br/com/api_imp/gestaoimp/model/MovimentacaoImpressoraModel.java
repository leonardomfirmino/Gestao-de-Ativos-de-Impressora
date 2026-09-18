package br.com.api_imp.gestaoimp.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_impressora")

public class MovimentacaoImpressoraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_MovImp;

    @ManyToOne
    @JoinColumn(name = "id_Imp_Antiga", nullable = false)
    private ImpressorasModel impressoraAntiga;

    @ManyToOne
    @JoinColumn(name = "id_local_Antiga", nullable = false)
    private LocalModel localAntiga;

    @ManyToOne
    @JoinColumn(name = "id_Imp_Nova", nullable = false)
    private ImpressorasModel impressoraNova;

    @ManyToOne
    @JoinColumn(name = "id_local_Nova", nullable = false)
    private LocalModel localNova;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio = LocalDateTime.now();

    @Column(name = "data_fim")
    private LocalDateTime dataFim;
    
    @Column(name = "descricao")
    private String descricao;

    public ImpressorasModel getImpressoraNova() {
        return impressoraNova;
    }
    public void setImpressoraNova(ImpressorasModel impressoraNova) {
        this.impressoraNova = impressoraNova;
    }
    public LocalModel getLocalNova() {
        return localNova;
    }
    public void setLocalNova(LocalModel localNova) {
        this.localNova = localNova;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public Long getId_MovImp() {
        return id_MovImp;
    }
    public ImpressorasModel getImpressora() {
        return impressoraAntiga;
    }
    public void setImpressora(ImpressorasModel impressoraAntiga) {
        this.impressoraAntiga = impressoraAntiga;
    }
    public LocalModel getLocal() {
        return localAntiga;
    }
    public void setLocal(LocalModel localAntiga) {
        this.localAntiga = localAntiga;
    }
    public LocalDateTime getDataInicio() {
        return dataInicio;
    }
    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }
    public LocalDateTime getDataFim() {
        return dataFim;
    }
    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }
    

    
}

