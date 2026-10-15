package cl.kioskonube.despachos.messaging.event;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Evento que llega desde ordenes-service (orden.creada).
 */
public record OrdenCreadaEvent(
        @NotNull Long ordenId,
        @NotBlank @Email String clienteEmail,
        BigDecimal total,
        List<Item> items,
        Instant fecha
) {
    public record Item(Long productoId, int cantidad) {
    }
}
