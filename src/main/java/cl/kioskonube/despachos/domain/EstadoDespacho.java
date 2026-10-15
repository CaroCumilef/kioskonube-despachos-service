package cl.kioskonube.despachos.domain;

/**
 * Estados de un despacho y las transiciones permitidas entre ellos.
 */
public enum EstadoDespacho {
    EN_PREPARACION,
    DESPACHADO,
    ENTREGADO,
    CANCELADO;

    public boolean puedePasarA(EstadoDespacho nuevo) {
        return switch (this) {
            case EN_PREPARACION -> nuevo == DESPACHADO || nuevo == CANCELADO;
            case DESPACHADO -> nuevo == ENTREGADO || nuevo == CANCELADO;
            case ENTREGADO, CANCELADO -> false;
        };
    }
}
