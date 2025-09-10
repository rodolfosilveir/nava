package br.com.nava.cooperfilme.exceptions;

public class ScriptStatusNotFoundException extends RuntimeException {
    public ScriptStatusNotFoundException(String text) {
        super("Script status '" + text + "' not found.");
    }
}
