package br.com.api_imp.gestaoimp.dto;

import br.com.api_imp.gestaoimp.model.UserModel;

public record UserResponseDTO(Long id, String name, String email, String role, boolean approved) {
    public UserResponseDTO(UserModel user) {
        this(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.isApproved());
    }
}
