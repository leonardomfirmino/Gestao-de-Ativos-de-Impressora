package br.com.api_imp.gestaoimp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.api_imp.gestaoimp.dto.ResponseUnidadeDTO;
import br.com.api_imp.gestaoimp.interfaces.UnidadeProjecao;
import br.com.api_imp.gestaoimp.model.LocalModel;

@Repository
public interface LocalRepository extends JpaRepository<LocalModel,Long> {
   
    @Query(value = "SELECT " +
               "  MIN(l.id_local) AS id, " +
               "  l.unidade AS name, " +
               "  COUNT(i.id_imp) AS printerCount " +
               "FROM locais l " +
               "LEFT JOIN impressora i ON l.id_local = i.id_local_atual " +
               "GROUP BY l.unidade", 
       nativeQuery = true)
    List<UnidadeProjecao> findUnidadesComTotalImpressoras();

    Optional<LocalModel> findByNomeLocal(String nome_local);

    Optional<LocalModel> findByNomeLocalAndUnidade(
        String nomeLocal,
        String unidade
    );

}
