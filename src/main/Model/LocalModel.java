import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locais")
@Getter
@Setter
public class LocalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocal;

    @Column(nullable = false)
    private String departamento;

    @Column(nullable = false)
    private String unidade;

    @Column(name = "nome_local", nullable = false)
    private String nomeLocal;

    
}
 
    

