package br.com.nava.cooperfilme.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record VoteScriptRequest (
    @NotNull(message = "A aprovação é obrigatória")
    @Schema(description = "Aprovação do roteiro", example = "true")
    Boolean approve,

    @Schema(description = "Justificativa sobre o roteiro", example = "Devem ser feitas alterações porque ...")
    String reason)

{}
