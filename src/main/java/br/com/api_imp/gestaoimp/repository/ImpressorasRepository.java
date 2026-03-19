package br.com.api_imp.gestaoimp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.api_imp.gestaoimp.model.ImpressorasModel;

@Repository
public interface ImpressorasRepository extends JpaRepository<ImpressorasModel,Long> {
    Optional<ImpressorasModel> findBySerialAndIp(String serial, String ip);
    
    Optional<ImpressorasModel> findBySerial(String serial);

    @Query(value = "SELECT DISTINCT modelo FROM impressoras ORDER BY modelo", nativeQuery = true)
    List<String> buscarModelos();

    @Query(value = "SELECT DISTINCT serial FROM impressoras ORDER BY serial", nativeQuery = true)
    List<String> buscarSerial();

    @Query("SELECT i.id_Imp FROM ImpressorasModel i WHERE i.serial = :serial")
    Optional<Long> findIdBySerial(@Param("serial") String serial);


    
    
}
