package br.com.api_imp.gestaoimp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;

@Repository
public interface MovimentacaoImpressoraRepository extends JpaRepository<MovimentacaoImpressoraModel, Long> {
    @Query(value = "SELECT DISTINCT m.* FROM movimentacao_impressora m " +
               "LEFT JOIN locais origem ON origem.id_local = m.id_local_origem " +
               "LEFT JOIN locais destino ON destino.id_local = m.id_local_destino " +
               "WHERE origem.unidade = (SELECT unidade FROM locais WHERE id_local = :idUnidade) " +
               "   OR destino.unidade = (SELECT unidade FROM locais WHERE id_local = :idUnidade) " +
               "ORDER BY m.data_movimentacao DESC", 
       nativeQuery = true)
    List<MovimentacaoImpressoraModel> findByUnidadeId(@Param("idUnidade") Long idUnidade);
}
