package cl.Inmobiliaria.catalogo_backend.dto;

import cl.Inmobiliaria.catalogo_backend.entity.Planta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantaResponse {
    private Long id;
    private String nombre;
    private Double precio;
    private String plano;
    private Integer dormitorios;
    private Integer banos;
    private Double supInterior;
    private Double supTerraza;
    private Double supTotal;
    private boolean disponible;

    public static PlantaResponse desde(Planta entidad) {
        if (entidad == null) return null;
        return PlantaResponse.builder()
                .id(entidad.getId())
                .nombre(entidad.getNombre())
                .precio(entidad.getPrecio())
                .plano(entidad.getPlano())
                .dormitorios(entidad.getDormitorios())
                .banos(entidad.getBanos())
                .supInterior(entidad.getSupInterior())
                .supTerraza(entidad.getSupTerraza())
                .supTotal(entidad.getSupTotal())
                .disponible(entidad.isDisponible())
                .build();
    }
}