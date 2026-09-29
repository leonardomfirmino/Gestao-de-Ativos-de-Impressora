package br.com.api_imp.gestaoimp.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api_imp.gestaoimp.dto.UserResponseDTO;
import br.com.api_imp.gestaoimp.service.UserService;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService users;
    public UserController(UserService users) { this.users = users; }
    @GetMapping public List<UserResponseDTO> list() { return users.list(); }
    @PatchMapping("/{id}/approve") public UserResponseDTO approve(@PathVariable Long id) { return users.approve(id); }
}
