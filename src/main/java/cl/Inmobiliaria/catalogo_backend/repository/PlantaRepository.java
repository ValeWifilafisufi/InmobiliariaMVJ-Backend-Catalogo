package cl.Inmobiliaria.catalogo_backend.repository;

import cl.Inmobiliaria.catalogo_backend.entity.Planta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantaRepository extends JpaRepository<Planta, Long> {
}
