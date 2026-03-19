package br.com.api_imp.gestaoimp.model;

import jakarta.persistence.*;


@Entity
@Table(name = "impressoras")
public class ImpressorasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id_Imp;
    
    @Column(length = 50)
    private String serial;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(name = "fila_impressao", length = 100)
    private String filaImpressao;

    @Column(name = "ip", length = 15)
    private String ip;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_atual", nullable = false)
    private StatusImpressoras statusAtual = StatusImpressoras.Estoque;

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setStatus(StatusImpressoras status) {
        this.statusAtual = status;
    }
    public StatusImpressoras getStatus() {
        return statusAtual;
    }
    public Long getId() {
        return id_Imp;
    }
    public void setId(Long id_Imp) {
        this.id_Imp = id_Imp;
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

