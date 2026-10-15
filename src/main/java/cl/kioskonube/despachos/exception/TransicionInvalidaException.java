package cl.kioskonube.despachos.exception;

import cl.kioskonube.despachos.domain.EstadoDespacho;

public class TransicionInvalidaException extends RuntimeException {

    public TransicionInvalidaException(EstadoDespacho actual, EstadoDespacho nuevo) {
        super("No se puede pasar de " + actual + " a " + nuevo);
    }
}
