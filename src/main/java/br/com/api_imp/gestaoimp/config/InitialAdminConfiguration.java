package br.com.api_imp.gestaoimp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.api_imp.gestaoimp.model.UserModel;
import br.com.api_imp.gestaoimp.repository.UserRepository;

@Configuration
public class InitialAdminConfiguration {
    @Bean
    CommandLineRunner createInitialAdmin(UserRepository users, PasswordEncoder passwordEncoder,
            @Value("${app.initial-admin.name:}") String name,
            @Value("${app.initial-admin.email:}") String email,
            @Value("${app.initial-admin.password:}") String password) {
        return args -> {
            if (name.isBlank() || email.isBlank() || password.isBlank() || users.existsByEmailIgnoreCase(email)) {
                return;
            }
            UserModel admin = new UserModel();
            admin.setUsername(name.trim());
            admin.setEmail(email.trim().toLowerCase());
            admin.setSenha(passwordEncoder.encode(password));
            admin.setRole("admin");
            admin.setApproved(true);
            users.save(admin);
        };
    }
}
