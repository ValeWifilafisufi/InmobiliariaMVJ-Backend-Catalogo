package cl.Inmobiliaria.catalogo_backend.service;

import cl.Inmobiliaria.catalogo_backend.dto.PlantaRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PlantaResponse;
import cl.Inmobiliaria.catalogo_backend.entity.Planta;
import cl.Inmobiliaria.catalogo_backend.entity.Propiedad;
import cl.Inmobiliaria.catalogo_backend.exception.PlantaNoEncontradaException;
import cl.Inmobiliaria.catalogo_backend.exception.PropiedadNoEncontradaException;
import cl.Inmobiliaria.catalogo_backend.repository.PlantaRepository;
import cl.Inmobiliaria.catalogo_backend.repository.PropiedadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlantaService {

    private final PlantaRepository plantaRepository;
    private final PropiedadRepository propiedadRepository;

    public PlantaService(PlantaRepository plantaRepository, PropiedadRepository propiedadRepository) {
        this.plantaRepository = plantaRepository;
        this.propiedadRepository = propiedadRepository;
    }

    @Transactional(readOnly = true)
    public PlantaResponse obtenerPorId(Long id) {
        Planta planta = plantaRepository.findById(id)
                .orElseThrow(() -> new PlantaNoEncontradaException(id));
        return PlantaResponse.desde(planta);
    }

    @Transactional
    public PlantaResponse agregarPlantaAProyecto(Long propiedadId, PlantaRequest request) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new PropiedadNoEncontradaException(propiedadId));

        if (!propiedad.isEsProyecto()) {
            throw new IllegalArgumentException("Solo los proyectos inmobiliarios pueden tener plantas asociadas.");
        }

        Planta nuevaPlanta = convertirAEntidad(request);
        nuevaPlanta.setId(null);

        propiedad.getPlantas().add(nuevaPlanta);
        Propiedad actualizada = propiedadRepository.save(propiedad);

        Planta guardada = actualizada.getPlantas().get(actualizada.getPlantas().size() - 1);
        return PlantaResponse.desde(guardada);
    }

    @Transactional
    public PlantaResponse actualizar(Long id, PlantaRequest request) {
        if (!plantaRepository.existsById(id)) {
            throw new PlantaNoEncontradaException(id);
        }

        Planta entidad = convertirAEntidad(request);
        entidad.setId(id);
        Planta actualizada = plantaRepository.save(entidad);
        return PlantaResponse.desde(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!plantaRepository.existsById(id)) {
            throw new PlantaNoEncontradaException(id);
        }
        plantaRepository.deleteById(id);
    }

    private Planta convertirAEntidad(PlantaRequest req) {
        if (req == null) return null;
        double interior = req.supInterior() != null ? req.supInterior() : 0.0;
        double terraza = req.supTerraza() != null ? req.supTerraza() : 0.0;
        double supTotal = Math.round((interior + terraza) * 100.0) / 100.0;
        boolean disp = req.disponible() != null ? req.disponible() : true;

        return Planta.builder()
                .id(req.id())
                .nombre(req.nombre())
                .precio(req.precio())
                .plano(req.plano())
                .dormitorios(req.dormitorios())
                .banos(req.banos())
                .supInterior(interior)
                .supTerraza(terraza)
                .supTotal(supTotal > 0 ? supTotal : (req.supTotal() != null ? req.supTotal() : 0.0))
                .disponible(disp)
                .build();
    }
}