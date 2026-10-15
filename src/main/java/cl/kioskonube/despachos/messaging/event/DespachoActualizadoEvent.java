package cl.kioskonube.despachos.messaging.event;

import java.time.Instant;

/**
 * Evento publicado en despachos.exchange (despacho.actualizado). Lo consume ordenes-service.
 * Sus campos coinciden con lo que espera ese servicio.
 */
public record DespachoActualizadoEvent(
        Long ordenId,
        Long despachoId,
        String estado,
        Instant fecha
) {
}
