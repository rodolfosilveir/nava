package br.com.nava.cooperfilme.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import br.com.nava.cooperfilme.api.requests.CreateScriptRequest;
import br.com.nava.cooperfilme.api.requests.CreateScriptRequestMock;
import br.com.nava.cooperfilme.api.requests.VoteScriptRequest;
import br.com.nava.cooperfilme.api.requests.VoteScriptRequestMock;
import br.com.nava.cooperfilme.api.responses.DefaultResponse;
import br.com.nava.cooperfilme.api.responses.ScriptIdResponse;
import br.com.nava.cooperfilme.api.responses.ScriptResponse;
import br.com.nava.cooperfilme.api.responses.ScriptStatusResponse;
import br.com.nava.cooperfilme.repositories.ScriptDetailsMock;
import br.com.nava.cooperfilme.services.ScriptControlService;
import br.com.nava.cooperfilme.services.ScriptService;

@ExtendWith(MockitoExtension.class)
class ScriptControllerTest {

    @Mock
    private ScriptService scriptService;

    @Mock
    private ScriptControlService scriptControlService;

    @InjectMocks
    private ScriptController scriptController;

    @Test
    @DisplayName("Deve criar um roteiro com sucesso, método createScript")
    void shouldCreateScriptSuccessfully() {
        CreateScriptRequest request = CreateScriptRequestMock.create();
        when(scriptService.createScript(request)).thenReturn(1);

        ResponseEntity<DefaultResponse<ScriptIdResponse>> response = scriptController.createScript(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(scriptService, times(1)).createScript(request);
    }

    @Test
    @DisplayName("Deve obter o status do roteiro com sucesso, método getScriptStatus")
    void shouldGetScriptStatusSuccessfully() {
        when(scriptService.getScriptStatus(1)).thenReturn("status");

        ResponseEntity<DefaultResponse<ScriptStatusResponse>> response = scriptController.getScriptStatus(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(scriptService, times(1)).getScriptStatus(1);
    }

    @Test
    @DisplayName("Deve obter os detalhes do roteiro com sucesso, método getScript")
    void shouldGetScriptSuccessfully() {
        ScriptResponse scriptResponse = ScriptResponse.fromDetails(ScriptDetailsMock.create());
        when(scriptService.getScriptDetails(1)).thenReturn(scriptResponse);

        ResponseEntity<DefaultResponse<ScriptResponse>> response = scriptController.getScript(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(scriptResponse, response.getBody().getResultData());
        verify(scriptService, times(1)).getScriptDetails(1);
    }

    @Test
    @DisplayName("Deve listar todos os roteiros com sucesso, método getAllScripts")
    void shouldGetAllScriptsSuccessfully() {
        ScriptResponse r1 = ScriptResponse.fromDetails(ScriptDetailsMock.create());
        ScriptResponse r2 = ScriptResponse.fromDetails(ScriptDetailsMock.create());
        List<ScriptResponse> list = List.of(r1, r2);
        when(scriptService.getAllScripts()).thenReturn(list);

        ResponseEntity<DefaultResponse<List<ScriptResponse>>> response = scriptController.getAllScripts();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(list, response.getBody().getResultData());
        verify(scriptService, times(1)).getAllScripts();
    }

    @Test
    @DisplayName("Deve votar no roteiro com sucesso, método voteScript")
    void shouldVoteScriptSuccessfully() {
        VoteScriptRequest request = VoteScriptRequestMock.create();

        ResponseEntity<DefaultResponse<Void>> response = scriptController.voteScript(1, request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(scriptControlService, times(1)).voteScript(1, request);
    }
}

