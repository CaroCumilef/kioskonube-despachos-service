package cl.kioskonube.despachos.messaging.producer;

import cl.kioskonube.despachos.config.MensajeriaProperties;
import cl.kioskonube.despachos.domain.Despacho;
import cl.kioskonube.despachos.exception.PublicacionException;
import cl.kioskonube.despachos.messaging.event.DespachoActualizadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Productor del despachos-service. Única clase que conoce RabbitTemplate.
 */
@Component
public class DespachoEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DespachoEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final MensajeriaProperties props;

    public DespachoEventPublisher(RabbitTemplate rabbitTemplate, MensajeriaProperties props) {
        this.rabbitTemplate = rabbitTemplate;
        this.props = props;
    }

    /** Si falla, lanza excepción y la transacción se revierte (el cambio no se guarda). */
    public void publicarActualizacion(Despacho despacho) {
        DespachoActualizadoEvent evento = new DespachoActualizadoEvent(
                despacho.getOrdenId(), despacho.getId(), despacho.getEstado().name(), Instant.now());
        try {
            rabbitTemplate.convertAndSend(props.despachos().exchange(), props.despachos().routingKey(), evento);
            log.info("Evento despacho.actualizado publicado despachoId={} ordenId={} estado={}",
                    despacho.getId(), despacho.getOrdenId(), despacho.getEstado());
        } catch (AmqpException e) {
            log.error("No se pudo publicar despacho.actualizado despachoId={}: {}", despacho.getId(), e.getMessage());
            throw new PublicacionException("No se pudo publicar el evento del despacho en RabbitMQ", e);
        }
    }
}
