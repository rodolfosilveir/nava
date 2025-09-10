package br.com.nava.cooperfilme.services;

import br.com.nava.cooperfilme.api.request.VoteScriptRequest;

public interface ScriptControlService {
    void voteScript(Integer id, VoteScriptRequest request);
}
