package cl.Inmobiliaria.catalogo_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.Inmobiliaria.catalogo_backend.entity.Propiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
    
    long countByEsProyectoAndDestacadaTrueAndDisponibleTrue(boolean esProyecto);
    
    long countByEsProyectoAndDestacadaTrueAndDisponibleTrueAndIdNot(boolean esProyecto, Long id);
}