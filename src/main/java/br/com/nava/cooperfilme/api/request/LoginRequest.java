package br.com.nava.cooperfilme.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    
    @NotBlank(message = "O login de usuário é obrigatório")
    @Schema(description = "Usuário cadastrado na aplicação", example = "admin")
    String login, 
    
    @Schema(description = "Senha do usuário cadastrado na aplicação", example = "admin123")
    String password) {
    
}
