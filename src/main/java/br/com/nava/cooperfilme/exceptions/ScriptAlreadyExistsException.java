package br.com.nava.cooperfilme.exceptions;

public class ScriptAlreadyExistsException extends RuntimeException {
    public ScriptAlreadyExistsException(String name) {
        super("Script already exists with name '" + name + "'.");
    }
}