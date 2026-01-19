import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "suprimentos")
@Getter
@Setter
public class SuprimentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSuprimento;

    @ManyToOne
    @JoinColumn(name = "serial_impressora", nullable = false)
    private ImpressorasModel impressora;

    @Column(name = "nivel_suprimento", nullable = false)
    private Integer nivelSuprimento;

    private LocalDate previsaoTermino;

    @Column(name = "ultima_atualizacao", nullable = false)
    private LocalDateTime ultimaAtualizacao = LocalDateTime.now();

    
}
