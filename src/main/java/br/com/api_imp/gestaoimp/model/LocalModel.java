package br.com.api_imp.gestaoimp.model;
import jakarta.persistence.*;

@Entity
@Table(name = "locais")

public class LocalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String departamento;

    @Column(nullable = false)
    private String unidade;

    @Column(name = "nome_local", nullable = false)
    private String nomeLocal;

    public String getNomeLocal() {
        return nomeLocal;
    }
    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public String getUnidade() {
        return unidade;
    }
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }


    
    
}
 
    

