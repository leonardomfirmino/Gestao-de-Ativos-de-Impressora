package br.com.api_imp.gestaoimp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.api_imp.gestaoimp.model.ImpressorasModel;

@Repository
public interface ImpressorasRepository extends JpaRepository<ImpressorasModel,Long> {
    Optional<ImpressorasModel> findBySerial(String serial);
    @Query(value = "SELECT DISTINCT fila_impressao FROM impressoras ORDER BY fila_impressao", nativeQuery = true)
    List<String> buscarFilasImpressao();

    @Query(value = "SELECT DISTINCT modelo FROM impressoras ORDER BY modelo", nativeQuery = true)
    List<String> buscarModelos();

    @Query(value = "SELECT DISTINCT serial FROM impressoras ORDER BY serial", nativeQuery = true)
    List<String> buscarSerial();

    @Query(value = "SELECT DISTINCT status FROM impressoras ORDER BY status", nativeQuery = true)
    List<String> buscarStatus();
    
}
