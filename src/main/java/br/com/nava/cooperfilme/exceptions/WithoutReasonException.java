package br.com.nava.cooperfilme.exceptions;

import br.com.nava.cooperfilme.dtos.ScriptStatus;

public class WithoutReasonException extends RuntimeException {
    public WithoutReasonException(ScriptStatus status) {
        super("reason: A reason is required when in the status: " + status.getText());
    }
}