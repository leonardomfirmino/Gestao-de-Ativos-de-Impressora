package br.com.api_imp.gestaoimp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.api_imp.gestaoimp.model.MovimentacaoImpressoraModel;

@Repository
public interface MovimentacaoImpressoraRepository extends JpaRepository<MovimentacaoImpressoraModel, Long> {
    @Query("SELECT m FROM MovimentacaoImpressoraModel m WHERE m.impressora.id_Imp = :id_Imp AND m.dataFim IS NULL")
    Optional<MovimentacaoImpressoraModel> buscarMovimentacaoAtiva(Long id_Imp);

    Optional<MovimentacaoImpressoraModel> findByImpressoraIdAndDataFimIsNull(Long impressoraId);
}
