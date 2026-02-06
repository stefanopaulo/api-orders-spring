package dev.projetos.stefano.order.api.resources.exceptions;

import java.io.Serial;

public class DatabaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8313442956431370901L;

    public DatabaseException(String msg) {
        super(msg);
    }
}
