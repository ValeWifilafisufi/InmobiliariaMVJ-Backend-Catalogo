package cl.Inmobiliaria.catalogo_backend.config;

import cl.Inmobiliaria.catalogo_backend.entity.Planta;
import cl.Inmobiliaria.catalogo_backend.entity.Propiedad;
import cl.Inmobiliaria.catalogo_backend.repository.PropiedadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PropiedadRepository propiedadRepository;

    @Override
    public void run(String... args) {
       
        if (propiedadRepository.count() == 0) {

            Propiedad p1 = Propiedad.builder()
                    .esProyecto(false)
                    .titulo("Casa Familiar Santiago")
                    .tipo("casa")
                    .ubicacion("Santiago")
                    .precio(2500.0)
                    .dormitorios(3)
                    .banos(2)
                    .metros(80.0)
                    .destacada(true)
                    .disponible(true)
                    .imagenes(List.of(
                            "/imagenes/casaFam1.jpg",
                            "/imagenes/casaFam2.jpg",
                            "/imagenes/casaFam3.jpg"
                    ))
                    .descripcion("Hermosa casa con patio, cercana a metro Santa Lucía y colegios.")
                    .build();

            Propiedad p2 = Propiedad.builder()
                    .esProyecto(false)
                    .titulo("Depto Vista Mar")
                    .tipo("departamento")
                    .ubicacion("Viña del Mar")
                    .precio(3100.0)
                    .dormitorios(2)
                    .banos(1)
                    .metros(55.0)
                    .destacada(true)
                    .disponible(true)
                    .imagenes(List.of(
                            "/imagenes/depaVina0.jpg",
                            "/imagenes/depaVina1.jpg",
                            "/imagenes/depaVina2.jpg",
                            "/imagenes/depaVina3.jpg"
                    ))
                    .descripcion("Departamento moderno con vista panorámica y piscina.")
                    .build();

            Propiedad p3 = Propiedad.builder()
                    .esProyecto(false)
                    .titulo("Departamento centro Quilpué")
                    .tipo("departamento")
                    .ubicacion("Quilpué")
                    .precio(3500.0)
                    .dormitorios(2)
                    .banos(2)
                    .metros(45.0)
                    .destacada(true)
                    .disponible(true)
                    .imagenes(List.of(
                            "/imagenes/depaQuilpue1.jpg",
                            "/imagenes/depaQuilpue2.jpg",
                            "/imagenes/depaQuilpue3.jpg"
                    ))
                    .descripcion("Departamento de firme construcción, cercano a plaza principal de la ciudad.")
                    .build();

            Propiedad p4 = Propiedad.builder()
                    .esProyecto(false)
                    .titulo("Casa 3 Pisos en Condominio")
                    .tipo("casa")
                    .ubicacion("Villa Alemana")
                    .precio(4800.0)
                    .dormitorios(4)
                    .banos(3)
                    .metros(120.0)
                    .destacada(true)
                    .disponible(true)
                    .imagenes(List.of(
                            "/imagenes/casa3pisos1.jpg",
                            "/imagenes/casa3pisos2.jpg",
                            "/imagenes/casa3pisos3.jpg",
                            "/imagenes/casa3pisos4.jpg"
                    ))
                    .descripcion("Espaciosa casa de 3 niveles en condominio cerrado con conserjería 24/7, plaza interior de juegos, quincho y excelente conectividad.")
                    .build();

            Propiedad p5 = Propiedad.builder()
                    .esProyecto(false)
                    .titulo("Loft Moderno Providencia")
                    .tipo("departamento")
                    .ubicacion("Santiago")
                    .precio(2900.0)
                    .dormitorios(1)
                    .banos(1)
                    .metros(42.0)
                    .destacada(false)
                    .disponible(true)
                    .imagenes(List.of("/imagenes/casaFam2.jpg"))
                    .descripcion("Loft estilo industrial a pasos de metro Los Leones, ideal para profesionales jóvenes o inversión.")
                    .build();

            Propiedad p6 = Propiedad.builder()
                    .esProyecto(false)
                    .titulo("Casa Campestre Chicureo")
                    .tipo("casa")
                    .ubicacion("Santiago")
                    .precio(6200.0)
                    .dormitorios(4)
                    .banos(4)
                    .metros(180.0)
                    .destacada(false)
                    .disponible(true)
                    .imagenes(List.of("/imagenes/casaFam3.jpg"))
                    .descripcion("Amplia casa estilo mediterráneo con gran terreno, quincho y piscina privada.")
                    .build();

            Propiedad p7 = Propiedad.builder()
                    .esProyecto(false)
                    .titulo("Depto Frente al Mar Concón")
                    .tipo("departamento")
                    .ubicacion("Concón")
                    .precio(4100.0)
                    .dormitorios(3)
                    .banos(2)
                    .metros(90.0)
                    .destacada(false)
                    .disponible(true)
                    .imagenes(List.of("/imagenes/depaVina1.jpg"))
                    .descripcion("Espectacular vista en primera línea de costa en zona gastronómica de Concón.")
                    .build();

            Propiedad p8 = Propiedad.builder()
                    .esProyecto(false)
                    .titulo("Casa con Piscina La Serena")
                    .tipo("casa")
                    .ubicacion("La Serena")
                    .precio(3800.0)
                    .dormitorios(3)
                    .banos(2)
                    .metros(110.0)
                    .destacada(false)
                    .disponible(true)
                    .imagenes(List.of("/imagenes/casaFam1.jpg"))
                    .descripcion("Hermosa propiedad residencial a minutos de la avenida del mar, amplia terraza.")
                    .build();

            Planta plantaA = Planta.builder()
                    .nombre("Planta A")
                    .precio(2500.0)
                    .plano("/imagenes/depto-A.png")
                    .dormitorios(3)
                    .banos(2)
                    .supInterior(103.2)
                    .supTerraza(28.13)
                    .supTotal(131.33)
                    .disponible(true)
                    .build();

            Planta plantaB = Planta.builder()
                    .nombre("Planta B")
                    .precio(2600.0)
                    .plano("/imagenes/depto-B.png")
                    .dormitorios(3)
                    .banos(2)
                    .supInterior(97.45)
                    .supTerraza(17.16)
                    .supTotal(114.61)
                    .disponible(true)
                    .build();

            Planta plantaC = Planta.builder()
                    .nombre("Planta C")
                    .precio(2900.0)
                    .plano("/imagenes/depto-C.png")
                    .dormitorios(3)
                    .banos(3)
                    .supInterior(98.89)
                    .supTerraza(20.24)
                    .supTotal(119.13)
                    .disponible(true)
                    .build();

            Planta plantaD = Planta.builder()
                    .nombre("Planta D")
                    .precio(2450.0)
                    .plano("/imagenes/depto-D.png")
                    .dormitorios(3)
                    .banos(2)
                    .supInterior(94.94)
                    .supTerraza(19.2)
                    .supTotal(114.14)
                    .disponible(true)
                    .build();

            Propiedad proyectoAltavista = Propiedad.builder()
                    .esProyecto(true)
                    .tipo("departamento")
                    .titulo("Condominio Altavista")
                    .ubicacion("Av. Principal 123, Viña del Mar")
                    .precio(2450.0)
                    .fechaEntrega("Por confirmar")
                    .descripcion("Excelente conectividad, 3 dormitorios y 3-2 baños.")
                    .imagenes(List.of(
                            "/imagenes/Luminus-fachada-luminus-1-768x512.jpg",
                            "/imagenes/Luminus-fachada2-768x512.jpg",
                            "/imagenes/interior1.jpg",
                            "/imagenes/interior2.jpg",
                            "/imagenes/interior3.jpg"
                    ))
                    .destacada(true)
                    .disponible(true)
                    .plantas(List.of(plantaA, plantaB, plantaC, plantaD))
                    .build();

            Planta casaQuillay = Planta.builder()
                    .nombre("Casa Quillay (con sala de estar)")
                    .precio(2500.0)
                    .plano("/imagenes/casa1.webp")
                    .dormitorios(3)
                    .banos(3)
                    .supInterior(61.48)
                    .supTerraza(23.36)
                    .supTotal(84.84)
                    .disponible(true)
                    .build();

            Planta casaMaqui = Planta.builder()
                    .nombre("Casa Maqui")
                    .precio(2500.0)
                    .plano("/imagenes/casa2.webp")
                    .dormitorios(3)
                    .banos(3)
                    .supInterior(54.2)
                    .supTerraza(21.0)
                    .supTotal(75.2)
                    .disponible(true)
                    .build();

            Planta casaLaurel = Planta.builder()
                    .nombre("Casa Laurel")
                    .precio(2500.0)
                    .plano("/imagenes/casa3Laurel.webp")
                    .dormitorios(3)
                    .banos(2)
                    .supInterior(48.64)
                    .supTerraza(16.49)
                    .supTotal(65.13)
                    .disponible(true)
                    .build();

            Propiedad proyectoVertientes = Propiedad.builder()
                    .esProyecto(true)
                    .tipo("casa")
                    .titulo("Condominio Las Vertientes")
                    .ubicacion("Calle Central 456")
                    .precio(2500.0)
                    .fechaEntrega("Por confirmar")
                    .descripcion("Hermosa vista, áreas verdes y piscina integrada.")
                    .imagenes(List.of(
                            "/imagenes/casasPorFuera.png",
                            "/imagenes/condominioCasas.webp",
                            "/imagenes/LivingCasas.webp",
                            "/imagenes/interiorcasas2.jpg",
                            "/imagenes/interiorcasas3.jpg"
                    ))
                    .destacada(true)
                    .disponible(true)
                    .plantas(List.of(casaQuillay, casaMaqui, casaLaurel))
                    .build();

            propiedadRepository.saveAll(List.of(
                    p1, p2, p3, p4, p5, p6, p7, p8,
                    proyectoAltavista, proyectoVertientes
            ));
        }
    }
}