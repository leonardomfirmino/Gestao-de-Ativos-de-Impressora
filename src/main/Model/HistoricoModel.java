import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="historio_status")
@Getter
@Setter
public class HistoricoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id_Status;

    @Column(name="Serial_Impressora")
    private String serial_impressora;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private StatusEnum status; 

    @Column(name = "Data_Inicial")
    private Date data_inicial;

    @Column(name = "Data_Final")
    private Date data_final;

    @Column(name = "Observacao")
    private String observacao;
}
