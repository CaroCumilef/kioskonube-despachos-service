package cl.kioskonube.despachos.dto;

import cl.kioskonube.despachos.domain.Despacho;

import java.time.Instant;

public record DespachoResponse(
        Long id,
        Long ordenId,
        String clienteEmail,
        String estado,
        String codigoSeguimiento,
        Instant fechaCreacion,
        Instant fechaActualizacion
) {

    public static DespachoResponse desde(Despacho d) {
        return new DespachoResponse(d.getId(), d.getOrdenId(), d.getClienteEmail(), d.getEstado().name(),
                d.getCodigoSeguimiento(), d.getFechaCreacion(), d.getFechaActualizacion());
    }
}
