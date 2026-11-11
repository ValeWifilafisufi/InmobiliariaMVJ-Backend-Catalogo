package cl.Inmobiliaria.catalogo_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
@Table(name = "plantas")
public class Planta {

    @Id 
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private double precio;
    private String plano;
    private int dormitorios;
    private int banos;
    private double supInterior;
    private double supTerraza;
    private double supTotal;
    private boolean disponible;
}