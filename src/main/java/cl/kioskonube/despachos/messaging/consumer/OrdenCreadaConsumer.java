package cl.kioskonube.despachos.messaging.consumer;

import cl.kioskonube.despachos.messaging.event.OrdenCreadaEvent;
import cl.kioskonube.despachos.service.DespachoService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Dominio ÓRDENES: cuando se crea una orden, se crea su despacho.
 */
@Component
public class OrdenCreadaConsumer {

    private final ProcesadorMensajes procesador;
    private final LectorMensajes lector;
    private final DespachoService despachoService;

    public OrdenCreadaConsumer(ProcesadorMensajes procesador, LectorMensajes lector,
                               DespachoService despachoService) {
        this.procesador = procesador;
        this.lector = lector;
        this.despachoService = despachoService;
    }

    @RabbitListener(id = "despachos-ordenes-consumer", queues = "${kioskonube.mensajeria.ordenes.queue}")
    public void recibir(Message message, Channel channel) throws IOException {
        procesador.procesar(message, channel, "orden-creada", cuerpo -> {
            OrdenCreadaEvent evento = lector.leerYValidar(cuerpo, OrdenCreadaEvent.class);
            despachoService.crearDesdeOrden(evento);
        });
    }
}
