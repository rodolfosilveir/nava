package br.com.nava.cooperfilme.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateScriptRequest (
    @NotBlank(message = "O nome do roteiro é obrigatório")
    @Schema(description = "Nome do roteiro", example = "O velho oeste")
    String name, 
    
    @NotBlank(message = "O conteúdo do roteiro é obrigatório")
    @Schema(description = "Conteúdo do roteiro", example = "Então os xerifes apontaram a arma e atiraram.")
    String content) 
    
{}
    

