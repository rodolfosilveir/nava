package br.com.nava.cooperfilme.services;

import java.util.List;

import br.com.nava.cooperfilme.api.request.CreateScriptRequest;
import br.com.nava.cooperfilme.api.responses.ScriptResponse;

public interface ScriptService {
    Integer createScript(CreateScriptRequest request);
    String getScriptStatus(Integer id);
    ScriptResponse getScriptDetails(Integer id);
    List<ScriptResponse> getAllScripts();

}
