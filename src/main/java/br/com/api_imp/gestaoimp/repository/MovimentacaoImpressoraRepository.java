package br.com.api_imp.gestaoimp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;

@Repository
public interface MovimentacaoImpressoraRepository extends JpaRepository<MovimentacaoImpressoraModel, Long> {

    @Query("SELECT m FROM MovimentacaoImpressoraModel m WHERE m.impressoraAntiga.id = :id_Imp_Antiga AND m.dataFim IS NULL")
    Optional<MovimentacaoImpressoraModel> buscarMovimentacaoAtiva(Long id_Imp_Antiga);

    @Query("SELECT m.impressoraAntiga.serial FROM MovimentacaoImpressoraModel m WHERE m.dataFim IS NULL OR m.dataFim > CURRENT_DATE")
    List<String> findByDataFimIsNull();

    @Query("SELECT m FROM MovimentacaoImpressoraModel m JOIN FETCH m.impressoraAntiga LEFT JOIN FETCH m.localAntiga")
    List<MovimentacaoImpressoraModel> findAllComDetalhes();
}
