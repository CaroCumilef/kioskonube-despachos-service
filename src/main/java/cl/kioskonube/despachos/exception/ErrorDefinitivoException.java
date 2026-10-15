package cl.kioskonube.despachos.exception;

/**
 * Error al procesar un mensaje que NO se arregla reintentando. El mensaje va directo a la DLQ.
 */
public class ErrorDefinitivoException extends RuntimeException {

    public ErrorDefinitivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
