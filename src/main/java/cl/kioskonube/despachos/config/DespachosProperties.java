package cl.kioskonube.despachos.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kioskonube.despachos")
public record DespachosProperties(@NotNull @Valid Reintentos reintentos) {

    public record Reintentos(@Min(1) int maxIntentos, @Min(0) long esperaMs) {
    }
}
