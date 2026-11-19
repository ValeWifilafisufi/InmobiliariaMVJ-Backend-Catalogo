package cl.Inmobiliaria.catalogo_backend.dto;

import cl.Inmobiliaria.catalogo_backend.entity.Propiedad;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropiedadResponse {
    private Long id;
    private boolean esProyecto;
    private String tipo;
    private String titulo;
    private String ubicacion;
    private Double precio;
    private String fechaEntrega;
    private String descripcion;
    private List<String> imagenes;
    private boolean destacada;
    private boolean disponible;
    private Integer dormitorios;
    private Integer banos;
    private Double metros;
    private List<PlantaResponse> plantas;

    public static PropiedadResponse desde(Propiedad entidad) {
        if (entidad == null) return null;
        List<PlantaResponse> plantasResp = entidad.getPlantas() == null
                ? List.of()
                : entidad.getPlantas().stream()
                        .map(PlantaResponse::desde)
                        .collect(Collectors.toList());

        return PropiedadResponse.builder()
                .id(entidad.getId())
                .esProyecto(entidad.isEsProyecto())
                .tipo(entidad.getTipo())
                .titulo(entidad.getTitulo())
                .ubicacion(entidad.getUbicacion())
                .precio(entidad.getPrecio())
                .fechaEntrega(entidad.getFechaEntrega())
                .descripcion(entidad.getDescripcion())
                .imagenes(entidad.getImagenes())
                .destacada(entidad.isDestacada())
                .disponible(entidad.isDisponible())
                .dormitorios(entidad.getDormitorios())
                .banos(entidad.getBanos())
                .metros(entidad.getMetros())
                .plantas(plantasResp)
                .build();
    }
}