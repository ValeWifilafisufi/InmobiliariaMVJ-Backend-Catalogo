package cl.Inmobiliaria.catalogo_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.Inmobiliaria.catalogo_backend.entity.Propiedad;

@Repository 
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
    
    long countByEsProyectoAndDestacadaTrueAndDisponibleTrue(boolean esProyecto);
    
    long countByEsProyectoAndDestacadaTrueAndDisponibleTrueAndIdNot(boolean esProyecto, Long id);
}