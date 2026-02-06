package dev.projetos.stefano.order.api.services.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -723877367551619330L;

    public ResourceNotFoundException (Object id) {
        super("Resource not found. Id: " + id);
    }
}
