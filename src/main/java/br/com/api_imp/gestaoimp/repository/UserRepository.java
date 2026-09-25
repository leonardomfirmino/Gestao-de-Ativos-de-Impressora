package br.com.api_imp.gestaoimp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api_imp.gestaoimp.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
