package br.com.api_imp.gestaoimp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api_imp.gestaoimp.model.HistoricoModel;

@Repository
public interface HistoricoRepository extends JpaRepository<HistoricoModel,Long> {
    
}
