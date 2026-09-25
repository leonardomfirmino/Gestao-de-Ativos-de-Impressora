package br.com.api_imp.gestaoimp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.api_imp.gestaoimp.dto.AuthResponseDTO;
import br.com.api_imp.gestaoimp.dto.LoginRequestDTO;
import br.com.api_imp.gestaoimp.dto.RegisterRequestDTO;
import br.com.api_imp.gestaoimp.dto.UserResponseDTO;
import br.com.api_imp.gestaoimp.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService users;
    public AuthController(UserService users) { this.users = users; }
    @PostMapping("/login") public AuthResponseDTO login(@RequestBody LoginRequestDTO request) { return users.login(request); }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody RegisterRequestDTO request) { return users.register(request); }
}
