package cl.kioskonube.despachos.dto;

import cl.kioskonube.despachos.domain.EstadoDespacho;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoRequest(
        @NotNull(message = "El estado es obligatorio (DESPACHADO, ENTREGADO o CANCELADO)")
        EstadoDespacho estado
) {
}
