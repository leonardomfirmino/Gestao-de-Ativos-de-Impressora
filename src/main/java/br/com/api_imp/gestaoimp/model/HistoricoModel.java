package br.com.api_imp.gestaoimp.model;
import lombok.*;
import java.util.*;
import jakarta.persistence.*;

@Entity
@Table(name="historio_status")
@Getter
@Setter
public class HistoricoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id_Status;

    @Column(name="Serial_Impressora")
    private String serial_impressora;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private StatusImpressoras status; 

    @Column(name = "Data_Inicial")
    private Date data_inicial;

    @Column(name = "Data_Final")
    private Date data_final;

    @Column(name = "Observacao")
    private String observacao;
}
