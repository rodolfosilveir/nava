package br.com.nava.cooperfilme.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.nava.cooperfilme.api.ScriptApi;
import br.com.nava.cooperfilme.api.requests.CreateScriptRequest;
import br.com.nava.cooperfilme.api.requests.VoteScriptRequest;
import br.com.nava.cooperfilme.api.responses.DefaultResponse;
import br.com.nava.cooperfilme.api.responses.ScriptIdResponse;
import br.com.nava.cooperfilme.api.responses.ScriptResponse;
import br.com.nava.cooperfilme.api.responses.ScriptStatusResponse;
import br.com.nava.cooperfilme.services.ScriptControlService;
import br.com.nava.cooperfilme.services.ScriptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ScriptController implements ScriptApi {

    private final ScriptService scriptService;
    private final ScriptControlService scriptControlService;

    @Override
    public ResponseEntity<DefaultResponse<ScriptIdResponse>> createScript(@RequestBody @Valid CreateScriptRequest request) {
        Integer id = scriptService.createScript(request);
        return ResponseEntity.ok(DefaultResponse.<ScriptIdResponse>builder()
            .httpStatus(200)
            .resultData(new ScriptIdResponse(id))
            .build());
    }

    @Override
    public ResponseEntity<DefaultResponse<ScriptStatusResponse>> getScriptStatus(Integer id) {
        String status = scriptService.getScriptStatus(id);
        return ResponseEntity.ok(DefaultResponse.<ScriptStatusResponse>builder()
            .httpStatus(200)
            .resultData(new ScriptStatusResponse(status))
            .build());
    }

    @Override
    public ResponseEntity<DefaultResponse<ScriptResponse>> getScript(Integer id) {
        ScriptResponse response = scriptService.getScriptDetails(id);
        return ResponseEntity.ok(DefaultResponse.<ScriptResponse>builder()
            .httpStatus(200)
            .resultData(response)
            .build());
    }

    @Override
    public ResponseEntity<DefaultResponse<List<ScriptResponse>>> getAllScripts() {

        List<ScriptResponse> responses = scriptService.getAllScripts();

        return ResponseEntity.ok(DefaultResponse.<List<ScriptResponse>>builder()
            .httpStatus(200)
            .resultData(responses)
            .build());
    }

    @Override
    public ResponseEntity<DefaultResponse<Void>> voteScript(Integer id, @Valid VoteScriptRequest request) {
        scriptControlService.voteScript(id, request);
        return ResponseEntity.ok(DefaultResponse.<Void>builder()
            .httpStatus(200)
            .build());
    }
    
}
