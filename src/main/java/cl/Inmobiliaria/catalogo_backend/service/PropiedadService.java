package cl.Inmobiliaria.catalogo_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.Inmobiliaria.catalogo_backend.dto.PlantaRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PropiedadRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PropiedadResponse;
import cl.Inmobiliaria.catalogo_backend.entity.Planta;
import cl.Inmobiliaria.catalogo_backend.entity.Propiedad;
import cl.Inmobiliaria.catalogo_backend.repository.PropiedadRepository;

@Service
public class PropiedadService {

    private final PropiedadRepository repositorio;

    public PropiedadService(PropiedadRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<PropiedadResponse> listar(String tipo, String ubicacion, String precioRango,
                                          Boolean esProyecto, Integer dormitorios, Integer banos, String metrosRango) {
        return repositorio.findAll().stream()
                .filter(p -> esProyecto == null || p.isEsProyecto() == esProyecto)
                .filter(p -> tipo == null || tipo.isBlank() || p.getTipo().equalsIgnoreCase(tipo))
                .filter(p -> ubicacion == null || ubicacion.isBlank() ||
                        p.getUbicacion().toLowerCase().contains(ubicacion.toLowerCase()) ||
                        p.getTitulo().toLowerCase().contains(ubicacion.toLowerCase()))
                .filter(p -> dormitorios == null || (p.getDormitorios() != null && (dormitorios >= 4 ? p.getDormitorios() >= 4 : p.getDormitorios().equals(dormitorios))))
                .filter(p -> banos == null || (p.getBanos() != null && (banos >= 3 ? p.getBanos() >= 3 : p.getBanos().equals(banos))))
                .filter(p -> filtrarPorPrecio(p.getPrecio(), precioRango))
                .filter(p -> filtrarPorMetros(p.getMetros(), metrosRango))
                .map(PropiedadResponse::desde)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PropiedadResponse obtenerPorId(Long id) {
        Propiedad entidad = repositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la propiedad con ID " + id));
        return PropiedadResponse.desde(entidad);
    }

    @Transactional
    public PropiedadResponse crear(PropiedadRequest req) {
        validarReglasDeNegocio(req);
        validarDestacadas(req, null);
        Propiedad entidad = convertirAEntidad(req);
        entidad.setId(null);
        Propiedad guardada = repositorio.save(entidad);
        return PropiedadResponse.desde(guardada);
    }

    @Transactional
    public PropiedadResponse actualizar(Long id, PropiedadRequest req) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("No existe la propiedad con ID " + id);
        }
        validarReglasDeNegocio(req);
        validarDestacadas(req, id);

        Propiedad entidad = convertirAEntidad(req);
        entidad.setId(id);
        Propiedad actualizada = repositorio.save(entidad);
        return PropiedadResponse.desde(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new RuntimeException("No existe la propiedad con ID " + id);
        }
        repositorio.deleteById(id);
    }

    private void validarReglasDeNegocio(PropiedadRequest req) {
        if (req.esProyecto()) {
            if (req.fechaEntrega() == null || req.fechaEntrega().isBlank()) {
                throw new IllegalArgumentException("La fecha de entrega es obligatoria para proyectos inmobiliarios.");
            }
        } else {
            if (req.dormitorios() == null || req.dormitorios() < 1) {
                throw new IllegalArgumentException("La cantidad de dormitorios es obligatoria para propiedades individuales.");
            }
            if (req.banos() == null || req.banos() < 1) {
                throw new IllegalArgumentException("La cantidad de baños es obligatoria para propiedades individuales.");
            }
            if (req.metros() == null || req.metros() <= 0) {
                throw new IllegalArgumentException("La superficie en m² es obligatoria y debe ser mayor a 0.");
            }
        }
    }

    private void validarDestacadas(PropiedadRequest req, Long idActual) {
        if (req.destacada() && req.disponible()) {
            long destacadasCount = (idActual == null)
                    ? repositorio.countByEsProyectoAndDestacadaTrueAndDisponibleTrue(req.esProyecto())
                    : repositorio.countByEsProyectoAndDestacadaTrueAndDisponibleTrueAndIdNot(req.esProyecto(), idActual);

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

    private boolean filtrarPorMetros(Double metros, String rango) {
        if (rango == null || rango.isBlank() || metros == null) return true;
        return switch (rango) {
            case "0-50" -> metros <= 50;
            case "50-100" -> metros >= 50 && metros <= 100;
            case "100-mas" -> metros > 100;
            default -> true;
        };
    }

    private Propiedad convertirAEntidad(PropiedadRequest req) {
        List<Planta> plantas = (req.plantas() == null) ? List.of() :
                req.plantas().stream()
                        .map(this::convertirPlantaAEntidad)
                        .collect(Collectors.toList());

        return Propiedad.builder()
                .esProyecto(req.esProyecto())
                .tipo(req.tipo())
                .titulo(req.titulo())
                .ubicacion(req.ubicacion())
                .precio(req.precio())
                .fechaEntrega(req.fechaEntrega())
                .descripcion(req.descripcion())
                .imagenes(req.imagenes() == null || req.imagenes().isEmpty()
                        ? List.of("/imagenes/casaFam1.jpg") : req.imagenes())
                .destacada(req.disponible() && req.destacada())
                .disponible(req.disponible())
                .dormitorios(req.dormitorios())
                .banos(req.banos())
                .metros(req.metros())
                .plantas(plantas)
                .build();
    }

    private Planta convertirPlantaAEntidad(PlantaRequest p) {
        if (p == null) return null;
        double interior = p.supInterior() != null ? p.supInterior() : 0.0;
        double terraza = p.supTerraza() != null ? p.supTerraza() : 0.0;
        double supTotal = Math.round((interior + terraza) * 100.0) / 100.0;
        boolean disp = p.disponible() != null ? p.disponible() : true;

        return Planta.builder()
                .id(p.id())
                .nombre(p.nombre())
                .precio(p.precio())
                .plano(p.plano())
                .dormitorios(p.dormitorios())
                .banos(p.banos())
                .supInterior(interior)
                .supTerraza(terraza)
                .supTotal(supTotal > 0 ? supTotal : (p.supTotal() != null ? p.supTotal() : 0.0))
                .disponible(disp)
                .build();
    }
}