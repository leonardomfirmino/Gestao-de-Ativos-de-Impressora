package br.com.api_imp.gestaoimp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.api_imp.gestaoimp.model.ImpressorasModel;

@Repository
public interface ImpressorasRepository extends JpaRepository<ImpressorasModel,Long> {
    
    boolean existsBySerial(String serial);

    @Query(value = "SELECT i.*"+
                "FROM impressora i"+
                "WHERE i.serial=:serial",nativeQuery = true)
    Optional <ImpressorasModel> findSerial(String serial);
    
    @Query(value = "SELECT i.*, l.unidade " +
               "FROM impressora i " +
               "INNER JOIN locais l ON i.id_local_atual = l.id_local " +
               "WHERE l.id_local = :id_local", nativeQuery = true)
    List<ImpressorasModel> findAllWithImpressoraUnidade(long id_local);

    @Query(value = "SELECT i.* FROM impressora i" +
                    "INNER JOIN locais l ON i.id_local_atual = l.id_local", nativeQuery = true)
    List<ImpressorasModel> findAllWithLocalAtual();



    
    
}
