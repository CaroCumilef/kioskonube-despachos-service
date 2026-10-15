package cl.kioskonube.despachos.domain;

import cl.kioskonube.despachos.exception.TransicionInvalidaException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

/**
 * Despacho asociado a una orden. Una orden tiene como máximo un despacho.
 */
@Entity
@Table(name = "despachos")
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orden_id", nullable = false, unique = true)
    private Long ordenId;

    @Column(name = "cliente_email", nullable = false, length = 150)
    private String clienteEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoDespacho estado;

    @Column(name = "codigo_seguimiento", nullable = false, unique = true, length = 20)
    private String codigoSeguimiento;

    @Column(name = "fecha_creacion", nullable = false)
    private Instant fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private Instant fechaActualizacion;

    protected Despacho() {
        // requerido por JPA
    }

    public Despacho(Long ordenId, String clienteEmail) {
        this.ordenId = ordenId;
        this.clienteEmail = clienteEmail;
        this.estado = EstadoDespacho.EN_PREPARACION;
        this.codigoSeguimiento = "KN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.fechaCreacion = Instant.now();
    }

    public void cambiarEstado(EstadoDespacho nuevo) {
        if (!estado.puedePasarA(nuevo)) {
            throw new TransicionInvalidaException(estado, nuevo);
        }
        this.estado = nuevo;
        this.fechaActualizacion = Instant.now();
    }

    public Long getId() { return id; }
    public Long getOrdenId() { return ordenId; }
    public String getClienteEmail() { return clienteEmail; }
    public EstadoDespacho getEstado() { return estado; }
    public String getCodigoSeguimiento() { return codigoSeguimiento; }
    public Instant getFechaCreacion() { return fechaCreacion; }
    public Instant getFechaActualizacion() { return fechaActualizacion; }
}
