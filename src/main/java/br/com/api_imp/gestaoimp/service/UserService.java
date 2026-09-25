package br.com.api_imp.gestaoimp.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.api_imp.gestaoimp.dto.AuthResponseDTO;
import br.com.api_imp.gestaoimp.dto.LoginRequestDTO;
import br.com.api_imp.gestaoimp.dto.RegisterRequestDTO;
import br.com.api_imp.gestaoimp.dto.UserResponseDTO;
import br.com.api_imp.gestaoimp.model.UserModel;
import br.com.api_imp.gestaoimp.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        validateLogin(request);
        UserModel user = users.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> unauthorized("E-mail ou senha inválidos."));
        boolean passwordMatches = passwordEncoder.matches(request.password(), user.getSenha());
        // Compatibilidade com usuários criados antes da introdução do BCrypt.
        if (!passwordMatches && request.password().equals(user.getSenha())) {
            user.setSenha(passwordEncoder.encode(request.password()));
            users.save(user);
            passwordMatches = true;
        }
        if (!passwordMatches) {
            throw unauthorized("E-mail ou senha inválidos.");
        }
        if (!user.isApproved()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Seu acesso ainda está pendente de aprovação.");
        }
        return new AuthResponseDTO(jwtService.generateToken(user), new UserResponseDTO(user));
    }

    public UserResponseDTO register(RegisterRequestDTO request) {
        if (request == null || blank(request.name()) || blank(request.email()) || blank(request.password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome, e-mail e senha são obrigatórios.");
        }
        if (request.password().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter pelo menos 8 caracteres.");
        }
        String email = request.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com este e-mail.");
        }
        UserModel user = new UserModel();
        user.setUsername(request.name().trim());
        user.setEmail(email);
        user.setSenha(passwordEncoder.encode(request.password()));
        user.setRole("user");
        user.setApproved(false);
        return new UserResponseDTO(users.save(user));
    }

    public List<UserResponseDTO> list() {
        return users.findAll().stream().map(UserResponseDTO::new).toList();
    }

    public UserResponseDTO approve(Long id) {
        UserModel user = users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        user.setApproved(true);
        return new UserResponseDTO(users.save(user));
    }

    private void validateLogin(LoginRequestDTO request) {
        if (request == null || blank(request.email()) || blank(request.password())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail e senha são obrigatórios.");
        }
    }
    private boolean blank(String value) { return value == null || value.isBlank(); }
    private ResponseStatusException unauthorized(String message) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
    }
}
