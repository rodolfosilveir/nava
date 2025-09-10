package br.com.nava.cooperfilme.api.requests;

public class CreateScriptRequestMock {

    public static CreateScriptRequest create() {
        return new CreateScriptRequest("name", "content");
    }
    
}
