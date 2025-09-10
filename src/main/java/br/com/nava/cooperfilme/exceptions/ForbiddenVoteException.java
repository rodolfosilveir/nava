package br.com.nava.cooperfilme.exceptions;

public class ForbiddenVoteException extends RuntimeException {

    public ForbiddenVoteException(String message) {
        super(message);
    }

}
