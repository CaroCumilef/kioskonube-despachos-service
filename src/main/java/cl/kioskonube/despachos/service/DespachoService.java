package cl.kioskonube.despachos.service;

import cl.kioskonube.despachos.domain.Despacho;
import cl.kioskonube.despachos.domain.EstadoDespacho;
import cl.kioskonube.despachos.dto.DespachoResponse;
import cl.kioskonube.despachos.exception.DespachoNoEncontradoException;
import cl.kioskonube.despachos.messaging.event.OrdenCreadaEvent;
import cl.kioskonube.despachos.messaging.producer.DespachoEventPublisher;
import cl.kioskonube.despachos.repository.DespachoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lógica de negocio de despachos. No conoce RabbitMQ: delega en DespachoEventPublisher.
 */
@Service
public class DespachoService {

    private static final Logger log = LoggerFactory.getLogger(DespachoService.class);

    private final DespachoRepository repository;
    private final DespachoEventPublisher publisher;

    public DespachoService(DespachoRepository repository, DespachoEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    /**
     * Crea el despacho de una orden nueva. Es idempotente: si el mensaje llega dos veces,
     * el segundo se ignora porque ya existe un despacho para esa orden.
     */
    @Transactional
    public void crearDesdeOrden(OrdenCreadaEvent evento) {
        if (repository.existsByOrdenId(evento.ordenId())) {
            log.info("Ya existe un despacho para la orden {}; se ignora el mensaje duplicado", evento.ordenId());
            return;
        }
        Despacho despacho = repository.save(new Despacho(evento.ordenId(), evento.clienteEmail()));
        log.info("Despacho creado id={} ordenId={} seguimiento={}",
                despacho.getId(), despacho.getOrdenId(), despacho.getCodigoSeguimiento());
        publisher.publicarActualizacion(despacho);
    }

    @Transactional
    public DespachoResponse cambiarEstado(Long id, EstadoDespacho nuevoEstado) {
        Despacho despacho = repository.findById(id)
                .orElseThrow(() -> new DespachoNoEncontradoException("No existe el despacho con id " + id));
        despacho.cambiarEstado(nuevoEstado);
        log.info("Despacho id={} cambió a {}", id, nuevoEstado);
        publisher.publicarActualizacion(despacho);
        return DespachoResponse.desde(despacho);
    }

    @Transactional(readOnly = true)
    public DespachoResponse obtener(Long id) {
        return repository.findById(id)
                .map(DespachoResponse::desde)
                .orElseThrow(() -> new DespachoNoEncontradoException("No existe el despacho con id " + id));
    }

    @Transactional(readOnly = true)
    public DespachoResponse buscarPorOrden(Long ordenId) {
        return repository.findByOrdenId(ordenId)
                .map(DespachoResponse::desde)
                .orElseThrow(() -> new DespachoNoEncontradoException("No existe despacho para la orden " + ordenId));
    }
}
