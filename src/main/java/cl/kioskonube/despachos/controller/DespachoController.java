package cl.kioskonube.despachos.controller;

import cl.kioskonube.despachos.dto.CambiarEstadoRequest;
import cl.kioskonube.despachos.dto.DespachoResponse;
import cl.kioskonube.despachos.service.DespachoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/despachos")
public class DespachoController {

    private final DespachoService service;

    public DespachoController(DespachoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public DespachoResponse obtener(@PathVariable @Positive(message = "El id debe ser positivo") Long id) {
        return service.obtener(id);
    }

    @GetMapping
    public DespachoResponse buscarPorOrden(
            @RequestParam @Positive(message = "El ordenId debe ser positivo") Long ordenId) {
        return service.buscarPorOrden(ordenId);
    }

    @PatchMapping("/{id}/estado")
    public DespachoResponse cambiarEstado(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id,
            @Valid @RequestBody CambiarEstadoRequest request) {
        return service.cambiarEstado(id, request.estado());
    }
}
