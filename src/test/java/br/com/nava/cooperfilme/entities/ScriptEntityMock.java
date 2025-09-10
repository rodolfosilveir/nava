package br.com.nava.cooperfilme.entities;

import java.util.UUID;

import br.com.nava.cooperfilme.dtos.ScriptStatus;

public class ScriptEntityMock {

    public static ScriptEntity create(UUID uuid, ScriptStatus status){ 
        return ScriptEntity.builder()
            .id(1)
            .idUserCreator(uuid)
            .name("Roteiro de Teste")
            .content("Conteúdo do roteiro de teste")
            .status(status)
            .build();
    }

}
