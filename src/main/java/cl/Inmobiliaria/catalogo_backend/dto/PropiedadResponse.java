package cl.Inmobiliaria.catalogo_backend.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
public class PropiedadDTO {
    private Long id;
    private boolean esProyecto;
    private String tipo;
    private String titulo;
    private String ubicacion;
    private double precio;
    private String fechaEntrega;
    private String descripcion;
    private List<String> imagenes;
    private boolean destacada;
    private boolean disponible;
    private Integer dormitorios;
    private Integer banos;
    private Double metros;
    private List<PlantaDTO> plantas;
}