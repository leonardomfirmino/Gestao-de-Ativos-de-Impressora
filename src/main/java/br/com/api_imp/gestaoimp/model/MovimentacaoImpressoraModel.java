package br.com.api_imp.gestaoimp.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_impressora")

public class MovimentacaoImpressoraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "serial_impressora", nullable = false)
    private ImpressorasModel impressora;

    @ManyToOne
    @JoinColumn(name = "id_local", nullable = false)
    private LocalModel local;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio = LocalDateTime.now();

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    public Long getId() {
        return id;
    }
    public ImpressorasModel getImpressora() {
        return impressora;
    }
    public void setImpressora(ImpressorasModel impressora) {
        this.impressora = impressora;
    }
    public LocalModel getLocal() {
        return local;
    }
    public void setLocal(LocalModel local) {
        this.local = local;
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

