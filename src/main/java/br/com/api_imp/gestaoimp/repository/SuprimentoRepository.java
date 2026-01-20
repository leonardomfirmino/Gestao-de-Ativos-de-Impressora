package br.com.api_imp.gestaoimp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api_imp.gestaoimp.model.SuprimentoModel;

@Repository
public interface SuprimentoRepository extends JpaRepository<SuprimentoModel,Long> {
    
}
