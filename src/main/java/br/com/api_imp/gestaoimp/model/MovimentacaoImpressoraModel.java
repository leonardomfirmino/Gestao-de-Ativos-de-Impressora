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
    @JoinColumn(name = "id_impressora")
    private ImpressorasModel impressora;

    @ManyToOne
    @JoinColumn(name = "id_local_origem")
    private LocalModel localOrigem;

    @ManyToOne
    @JoinColumn(name = "id_local_destino")
    private LocalModel localDestino;

    private LocalDateTime dataMovimentacao;
    private String descricaoMotivo;

    public Long getId() {
        return id;
    }

    public ImpressorasModel getImpressora() {
        return impressora;
    }
    public void setImpressora(ImpressorasModel impressora) {
        this.impressora = impressora;
    }
    public LocalModel getLocalOrigem() {
        return localOrigem;
    }
    public void setLocalOrigem(LocalModel localOrigem) {
        this.localOrigem = localOrigem;
    }
    public LocalModel getLocalDestino() {
        return localDestino;
    }
    public void setLocalDestino(LocalModel localDestino) {
        this.localDestino = localDestino;
    }
    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }
    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }
    public String getDescricaoMotivo() {
        return descricaoMotivo;
    }
    public void setDescricaoMotivo(String descricaoMotivo) {
        this.descricaoMotivo = descricaoMotivo;
    }

    
    

    
}

