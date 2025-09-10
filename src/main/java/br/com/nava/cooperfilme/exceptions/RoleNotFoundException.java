package br.com.nava.cooperfilme.exceptions;

import br.com.nava.cooperfilme.dtos.Role;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(final String text) {
        super(String.format("Role '%s' não permitida. Roles permitidas: %s", text, Role.getDescriptions()));
    }
    
}
