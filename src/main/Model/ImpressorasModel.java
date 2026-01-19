import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "impressoras")
@Getter
@Setter
public class ImpressorasModel {

    @Id
    private String serial;

    @Column(nullable = false)
    private String modelo;

    @Column(name = "fila_impressao", nullable = false)
    private String filaImpressao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_atual", nullable = false)
    private StatusImpressoras statusAtual = StatusImpressoras.Alocado;

    
}
