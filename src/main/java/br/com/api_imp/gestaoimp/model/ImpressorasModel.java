package br.com.api_imp.gestaoimp.model;
import lombok.*;
import jakarta.persistence.*;
@Entity
@Table(name = "impressoras")
@Getter
@Setter
public class ImpressorasModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_impressora;
    
    @Column(nullable = false)
    private String serial;

    @Column(nullable = false)
    private String modelo;

    @Column(name = "fila_impressao", nullable = false)
    private String filaImpressao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_atual", nullable = false)
    private StatusImpressoras statusAtual = StatusImpressoras.Alocado;

    
}
