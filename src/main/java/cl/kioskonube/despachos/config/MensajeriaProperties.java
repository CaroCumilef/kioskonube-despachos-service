package cl.kioskonube.despachos.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Nombres de exchanges, colas y routing keys del despachos-service.
 */
@Validated
@ConfigurationProperties(prefix = "kioskonube.mensajeria")
public record MensajeriaProperties(
        @NotNull @Valid Publicacion despachos,
        @NotNull @Valid Suscripcion ordenes,
        @NotNull @Valid DeadLetter deadLetter
) {

    public record Publicacion(@NotBlank String exchange, @NotBlank String routingKey) {
    }

    public record Suscripcion(@NotBlank String exchange, @NotBlank String queue, @NotBlank String routingKey) {
    }

    public record DeadLetter(@NotBlank String dlxSufijo, @NotBlank String dlqSufijo) {
    }

    public String dlxDe(String queue) {
        return queue + deadLetter.dlxSufijo();
    }

    public String dlqDe(String queue) {
        return queue + deadLetter.dlqSufijo();
    }
}
