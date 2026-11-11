package cl.Inmobiliaria.catalogo_backend.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 
@Entity 
@Table(name = "propiedad")
public class Propiedad {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    private boolean esProyecto;
    private String tipo;
    private String titulo;
    private String ubicacion;
    private double precio;
    private String fechaEntrega;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ElementCollection 
    @CollectionTable(name = "propiedad_imagenes", joinColumns = @JoinColumn(name = "propiedad_id"))
    @Column(name = "imagen_url")
    @Builder.Default
    private List<String> imagenes = new ArrayList<>();

    private boolean destacada;
    private boolean disponible;
    private Integer dormitorios;
    private Integer banos;
    private Double metros;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "propiedad_id")
    @Builder.Default
    private List<Planta> plantas = new ArrayList<>();
}