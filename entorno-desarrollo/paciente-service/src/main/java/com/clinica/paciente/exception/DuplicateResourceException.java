package com.clinica.paciente.exception;

/**
 * Excepción lanzada cuando se intenta crear un recurso duplicado
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
