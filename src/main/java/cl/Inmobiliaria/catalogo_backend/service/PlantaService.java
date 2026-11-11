package cl.Inmobiliaria.catalogo_backend.service;

import cl.Inmobiliaria.catalogo_backend.dto.PlantaDTO;
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
    public PlantaDTO obtenerPorId(Long id) {
        Planta planta = plantaRepository.findById(id)
                .orElseThrow(() -> new PlantaNoEncontradaException(id));
        return convertirADTO(planta);
    }

    @Transactional
    public PlantaDTO agregarPlantaAProyecto(Long propiedadId, PlantaDTO plantaDTO) {
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new PropiedadNoEncontradaException(propiedadId));

        if (!propiedad.isEsProyecto()) {
            throw new IllegalArgumentException("Solo los proyectos inmobiliarios pueden tener plantas asociadas.");
        }

        Planta nuevaPlanta = convertirAEntidad(plantaDTO);
        nuevaPlanta.setId(null);
        
        propiedad.getPlantas().add(nuevaPlanta);
        Propiedad actualizada = propiedadRepository.save(propiedad);

        Planta guardada = actualizada.getPlantas().get(actualizada.getPlantas().size() - 1);
        return convertirADTO(guardada);
    }

    @Transactional
    public PlantaDTO actualizar(Long id, PlantaDTO dto) {
        if (!plantaRepository.existsById(id)) {
            throw new PlantaNoEncontradaException(id);
        }

        Planta entidad = convertirAEntidad(dto);
        entidad.setId(id);
        Planta actualizada = plantaRepository.save(entidad);
        return convertirADTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!plantaRepository.existsById(id)) {
            throw new PlantaNoEncontradaException(id);
        }
        plantaRepository.deleteById(id);
    }

    private PlantaDTO convertirADTO(Planta p) {
        if (p == null) return null;
        return PlantaDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .precio(p.getPrecio())
                .plano(p.getPlano())
                .dormitorios(p.getDormitorios())
                .banos(p.getBanos())
                .supInterior(p.getSupInterior())
                .supTerraza(p.getSupTerraza())
                .supTotal(p.getSupTotal())
                .disponible(p.isDisponible())
                .build();
    }

    private Planta convertirAEntidad(PlantaDTO dto) {
        if (dto == null) return null;
        double supTotal = Math.round((dto.getSupInterior() + dto.getSupTerraza()) * 100.0) / 100.0;
        return Planta.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .plano(dto.getPlano())
                .dormitorios(dto.getDormitorios())
                .banos(dto.getBanos())
                .supInterior(dto.getSupInterior())
                .supTerraza(dto.getSupTerraza())
                .supTotal(supTotal > 0 ? supTotal : dto.getSupTotal())
                .disponible(dto.isDisponible())
                .build();
    }
}
