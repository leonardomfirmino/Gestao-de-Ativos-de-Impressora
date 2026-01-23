package br.com.api_imp.gestaoimp.model;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "suprimentos")

public class SuprimentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "serial_impressora", nullable = false)
    private ImpressorasModel impressora;

    @Column(name = "nivel_suprimento", nullable = false)
    private Integer nivelSuprimento;

    private LocalDate previsaoTermino;

    @Column(name = "ultima_atualizacao", nullable = false)
    private LocalDateTime ultimaAtualizacao = LocalDateTime.now();

    @Column(name = "tipo_suprimento", nullable = false)
    private String tipoSuprimento;

    public Long getId() {
        return id;
    }   
    public ImpressorasModel getImpressora() {
        return impressora;
    }
    public void setImpressora(ImpressorasModel impressora) {
        this.impressora = impressora;
    }
    public Integer getNivelSuprimento() {
        return nivelSuprimento;
    }
    public void setNivelSuprimento(Integer nivelSuprimento) {
        this.nivelSuprimento = nivelSuprimento;
    }
    public LocalDate getPrevisaoTermino() {
        return previsaoTermino;
    }
    public void setPrevisaoTermino(LocalDate previsaoTermino) {
        this.previsaoTermino = previsaoTermino;
    }
    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }
    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }
    public String getTipoSuprimento() {
        return tipoSuprimento;
    }
    public void setTipoSuprimento(String tipoSuprimento) {
        this.tipoSuprimento = tipoSuprimento;
    }
    
}
