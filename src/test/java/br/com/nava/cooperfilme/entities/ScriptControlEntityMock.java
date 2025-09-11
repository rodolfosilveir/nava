package br.com.nava.cooperfilme.entities;

import java.util.UUID;

public class ScriptControlEntityMock {

    public static ScriptControlEntity create() {
        return ScriptControlEntity.builder()
        .build();
    }

    public static ScriptControlEntity create(UUID uuidVote1, UUID uuidVote2, boolean vote1, boolean vote2) {
        return ScriptControlEntity.builder()
            .approvedVote1ById(uuidVote1)
            .approvedVote2ById(uuidVote2)
            .approvedVote1(vote1)
            .approvedVote2(vote2)
        .build();
    }

}
