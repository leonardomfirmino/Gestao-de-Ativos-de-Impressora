package br.com.api_imp.gestaoimp.model;

import jakarta.persistence.*;

@Entity 
@Table(name = "usuario")
public class UserModel {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  
                 
    @Column (name="userName")
    private String username;

    @Column (name="email", nullable = false, unique = true)
    private String email;

    @Column (name="senha", nullable = false)
    private String senha;

    @Column(nullable = false, length = 20)
    private String role = "user";

    @Column(nullable = false)
    private boolean approved = false;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public boolean isApproved() {
        return approved;
    }
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    
}
