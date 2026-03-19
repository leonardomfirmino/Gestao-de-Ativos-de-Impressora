package br.com.api_imp.gestaoimp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.api_imp.gestaoimp.model.LocalModel;

@Repository
public interface LocalRepository extends JpaRepository<LocalModel,Long> {
    @Query(value = "SELECT DISTINCT unidade FROM locais ORDER BY unidade", nativeQuery = true)
    List<String> buscarUnidades();

    @Query(value = "SELECT DISTINCT nome_local FROM locais ORDER BY nome_local", nativeQuery = true)
    List<String> buscarLocais();

    Optional<LocalModel> findByNomeLocal(String nome_local);

    Optional<LocalModel> findByNomeLocalAndUnidade(
        String nomeLocal,
        String unidade
    );
}