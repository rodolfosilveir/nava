package br.com.nava.cooperfilme.api.requests;

public class VoteScriptRequestMock {

    public static VoteScriptRequest create() {
        return new VoteScriptRequest(true, "reason");
    }

}
