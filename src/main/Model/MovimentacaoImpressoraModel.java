import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_impressora")
@Getter
@Setter
public class MovimentacaoImpressoraModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimentacao;

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

    
}

