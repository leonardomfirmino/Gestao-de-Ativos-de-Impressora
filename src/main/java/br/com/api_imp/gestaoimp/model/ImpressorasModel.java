package br.com.api_imp.gestaoimp.model;

import jakarta.persistence.*;


@Entity
@Table(name = "impressoras")
public class ImpressorasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;
    
    @Column(length = 50)
    private String serial;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(name = "fila_impressao", nullable = false, length = 100)
    private String filaImpressao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_atual", nullable = false)
    private StatusImpressoras statusAtual = StatusImpressoras.Estoque;
    public Long getId() {
        return id;
    }
    public String getSerial() {
        return serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getFilaImpressao() {
        return filaImpressao;
    }
    public void setFilaImpressao(String filaImpressao) {
        this.filaImpressao = filaImpressao;
    }
    public StatusImpressoras getStatusAtual() {
        return statusAtual;
    }
    public void setStatusAtual(StatusImpressoras statusAtual) {
        this.statusAtual = statusAtual;
    }
    
}

