package cl.kioskonube.despachos.repository;

import cl.kioskonube.despachos.domain.Despacho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DespachoRepository extends JpaRepository<Despacho, Long> {

    Optional<Despacho> findByOrdenId(Long ordenId);

    boolean existsByOrdenId(Long ordenId);
}
