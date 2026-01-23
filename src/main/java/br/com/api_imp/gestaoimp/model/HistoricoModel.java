package br.com.api_imp.gestaoimp.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "historico_status")
public class HistoricoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "serial_impressora", nullable = false)
    private ImpressorasModel impressora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusImpressoras status;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio = LocalDateTime.now();

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Column(length = 255)
    private String observacao;

    public Long getId() {
        return id;
    }
    public ImpressorasModel getImpressora() {
        return impressora;
    }
    public void setImpressora(ImpressorasModel impressora) {
        this.impressora = impressora;
    }
    public StatusImpressoras getStatus() {
        return status;
    }
    public void setStatus(StatusImpressoras status) {
        this.status = status;
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
    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    
}
