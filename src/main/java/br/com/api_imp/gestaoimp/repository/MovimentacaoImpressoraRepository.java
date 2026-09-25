package br.com.api_imp.gestaoimp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;

@Repository
public interface MovimentacaoImpressoraRepository extends JpaRepository<MovimentacaoImpressoraModel, Long> {
    @Query(value = "SELECT m.* FROM movimentacao_impressora m " +
               "WHERE m.id_local_origem = :idUnidade " +
               "   OR m.id_local_destino = :idUnidade " +
               "ORDER BY m.data_movimentacao DESC", 
       nativeQuery = true)
    List<MovimentacaoImpressoraModel> findByUnidadeId(@Param("idUnidade") Long idUnidade);
}
