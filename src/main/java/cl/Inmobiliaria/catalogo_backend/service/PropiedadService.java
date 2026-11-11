package cl.Inmobiliaria.catalogo_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import cl.Inmobiliaria.catalogo_backend.entity.Planta;
import cl.Inmobiliaria.catalogo_backend.dto.PlantaDTO;
import cl.Inmobiliaria.catalogo_backend.dto.PropiedadDTO;
import java.util.stream.Collectors;
import cl.Inmobiliaria.catalogo_backend.entity.Propiedad;
import cl.Inmobiliaria.catalogo_backend.repository.PropiedadRepository;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class PropiedadService {

    private final PropiedadRepository repositorio;

    public PropiedadService(PropiedadRepository repositorio){
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<PropiedadDTO> listar(String tipo, String ubicacion, String precioRango, Boolean esProyecto) {
        return repositorio.findAll().stream()
                .filter(p -> esProyecto == null || p.isEsProyecto() == esProyecto)
                .filter(p -> tipo == null || tipo.isBlank() || p.getTipo().equalsIgnoreCase(tipo))
                .filter(p -> ubicacion == null || ubicacion.isBlank() ||
                        p.getUbicacion().toLowerCase().contains(ubicacion.toLowerCase()) ||
                        p.getTitulo().toLowerCase().contains(ubicacion.toLowerCase()))
                .filter(p -> filtrarPorPrecio(p.getPrecio(), precioRango))
                .map(this::convertirADTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PropiedadDTO obtenerPorId(Long id) {
        Propiedad entidad = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la propiedad con ID " + id));
        return convertirADTO(entidad);
    }

    @Transactional
    public PropiedadDTO crear(PropiedadDTO dto) {
        validarDestacadas(dto, null);
        Propiedad entidad = convertirAEntidad(dto);
        entidad.setId(null); // Garantiza que sea una creación nueva
        Propiedad guardada = repositorio.save(entidad);
        return convertirADTO(guardada);
    }

    @Transactional
    public PropiedadDTO actualizar(Long id, PropiedadDTO dto) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("No existe la propiedad con ID " + id);
        }
        validarDestacadas(dto, id);

        Propiedad entidad = convertirAEntidad(dto);
        entidad.setId(id);
        Propiedad actualizada = repositorio.save(entidad);
        return convertirADTO(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("No existe la propiedad con ID " + id);
        }
        repositorio.deleteById(id);
    }

    private void validarDestacadas(PropiedadDTO dto, Long idActual) {
        if (dto.isDestacada() && dto.isDisponible()) {
            long destacadasCount = (idActual == null)
                    ? repositorio.countByEsProyectoAndDestacadaTrueAndDisponibleTrue(dto.isEsProyecto())
                    : repositorio.countByEsProyectoAndDestacadaTrueAndDisponibleTrueAndIdNot(dto.isEsProyecto(), idActual);

            if (destacadasCount >= 4) {
                throw new IllegalArgumentException("Límite alcanzado: Máximo 4 destacadas permitidas.");
            }
        }
    }

    private boolean filtrarPorPrecio(double precio, String rango) {
        if (rango == null || rango.isBlank()) return true;
        return switch (rango) {
            case "0-2500" -> precio <= 2500;
            case "2500-3000" -> precio >= 2500 && precio <= 3000;
            case "3000-mas" -> precio > 3000;
            default -> true;
        };
    }

    private PropiedadDTO convertirADTO(Propiedad e) {
        List<PlantaDTO> plantasDTO = (e.getPlantas() == null) ? List.of() :
                e.getPlantas().stream()
                        .map(this::convertirPlantaADTO)
                        .collect(Collectors.toList());

        return PropiedadDTO.builder()
                .id(e.getId())
                .esProyecto(e.isEsProyecto())
                .tipo(e.getTipo())
                .titulo(e.getTitulo())
                .ubicacion(e.getUbicacion())
                .precio(e.getPrecio())
                .fechaEntrega(e.getFechaEntrega())
                .descripcion(e.getDescripcion())
                .imagenes(e.getImagenes())
                .destacada(e.isDestacada())
                .disponible(e.isDisponible())
                .dormitorios(e.getDormitorios())
                .banos(e.getBanos())
                .metros(e.getMetros())
                .plantas(plantasDTO)
                .build();
    }

    private Propiedad convertirAEntidad(PropiedadDTO dto) {
        List<Planta> plantas = (dto.getPlantas() == null) ? List.of() :
                dto.getPlantas().stream()
                        .map(this::convertirPlantaAEntidad)
                        .collect(Collectors.toList());

        return Propiedad.builder()
                .id(dto.getId())
                .esProyecto(dto.isEsProyecto())
                .tipo(dto.getTipo())
                .titulo(dto.getTitulo())
                .ubicacion(dto.getUbicacion())
                .precio(dto.getPrecio())
                .fechaEntrega(dto.getFechaEntrega())
                .descripcion(dto.getDescripcion())
                .imagenes(dto.getImagenes() == null || dto.getImagenes().isEmpty() 
                        ? List.of("/imagenes/casaFam1.jpg") : dto.getImagenes())
                .destacada(dto.isDisponible() && dto.isDestacada())
                .disponible(dto.isDisponible())
                .dormitorios(dto.getDormitorios())
                .banos(dto.getBanos())
                .metros(dto.getMetros())
                .plantas(plantas)
                .build();
    }

    private PlantaDTO convertirPlantaADTO(Planta p) {
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

    private Planta convertirPlantaAEntidad(PlantaDTO p) {
        if (p == null) return null;
        double supTotal = Math.round((p.getSupInterior() + p.getSupTerraza()) * 100.0) / 100.0;
        return Planta.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .precio(p.getPrecio())
                .plano(p.getPlano())
                .dormitorios(p.getDormitorios())
                .banos(p.getBanos())
                .supInterior(p.getSupInterior())
                .supTerraza(p.getSupTerraza())
                .supTotal(supTotal > 0 ? supTotal : p.getSupTotal())
                .disponible(p.isDisponible())
                .build();
    }
}