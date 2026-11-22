package cl.Inmobiliaria.catalogo_backend.service;

import cl.Inmobiliaria.catalogo_backend.dto.PlantaRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PlantaResponse;
import cl.Inmobiliaria.catalogo_backend.entity.Planta;
import cl.Inmobiliaria.catalogo_backend.entity.Propiedad;
import cl.Inmobiliaria.catalogo_backend.exception.PlantaNoEncontradaException;
import cl.Inmobiliaria.catalogo_backend.exception.PropiedadNoEncontradaException;
import cl.Inmobiliaria.catalogo_backend.repository.PlantaRepository;
import cl.Inmobiliaria.catalogo_backend.repository.PropiedadRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - PlantaService")
class PlantaServiceTest {

    @Mock
    private PlantaRepository plantaRepository;

    @Mock
    private PropiedadRepository propiedadRepository;

    @InjectMocks
    private PlantaService plantaService;

    private final Faker faker = new Faker(Locale.forLanguageTag("es-CL"));

    private Planta plantaMock;
    private PlantaRequest plantaRequestMock;
    private Propiedad proyectoMock;
    private Propiedad propiedadIndividualMock;
    private Long plantaIdMock;
    private Long proyectoIdMock;

    @BeforeEach
    void setUp() {
        plantaIdMock = 1L;
        proyectoIdMock = 10L;

        String nombrePlanta = "Planta Tipo " + faker.letterify("?").toUpperCase();

        plantaMock = Planta.builder()
                .id(plantaIdMock)
                .nombre(nombrePlanta)
                .precio(3500.0)
                .plano("/imagenes/plantaA.jpg")
                .dormitorios(2)
                .banos(2)
                .supInterior(58.0)
                .supTerraza(8.5)
                .supTotal(66.5)
                .disponible(true)
                .build();

        plantaRequestMock = new PlantaRequest(
                null,
                nombrePlanta,
                3500.0,
                "/imagenes/plantaA.jpg",
                2,
                2,
                58.0,
                8.5,
                null,
                true
        );

        proyectoMock = Propiedad.builder()
                .id(proyectoIdMock)
                .esProyecto(true)
                .tipo("Condominio")
                .titulo("Condominio " + faker.company().name())
                .ubicacion("Quilpué")
                .plantas(new ArrayList<>())
                .build();

        propiedadIndividualMock = Propiedad.builder()
                .id(20L)
                .esProyecto(false)
                .tipo("Casa")
                .titulo("Casa " + faker.address().streetName())
                .ubicacion("Villa Alemana")
                .plantas(new ArrayList<>())
                .build();
    }

    //obtenerPorId()

    @Test
    @DisplayName("Debe retornar la planta")
    void obtenerPorId() {

        when(plantaRepository.findById(plantaIdMock)).thenReturn(Optional.of(plantaMock));

        PlantaResponse resultado = plantaService.obtenerPorId(plantaIdMock);

        assertNotNull(resultado);
        assertEquals(plantaIdMock, resultado.getId());
        assertEquals(plantaMock.getNombre(), resultado.getNombre());
        assertEquals(66.5, resultado.getSupTotal());
    }


    //agregar()

    @Test
    @DisplayName("Debe agregar una planta a un proyecto existente")
    void agregarPlantaAProyecto() {

        when(propiedadRepository.findById(proyectoIdMock)).thenReturn(Optional.of(proyectoMock));
        when(propiedadRepository.save(any(Propiedad.class))).thenAnswer(inv -> {
            Propiedad p = inv.getArgument(0);
            p.getPlantas().get(p.getPlantas().size() - 1).setId(5L);
            return p;
        });

        PlantaResponse resultado = plantaService.agregarPlantaAProyecto(proyectoIdMock, plantaRequestMock);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        verify(propiedadRepository).save(proyectoMock);
    }

    //actualizar()

    @Test
    @DisplayName("Debe actualizar la planta")
    void actualizar() {

        when(plantaRepository.existsById(plantaIdMock)).thenReturn(true);
        when(plantaRepository.save(any(Planta.class))).thenReturn(plantaMock);

        PlantaResponse resultado = plantaService.actualizar(plantaIdMock, plantaRequestMock);

        assertNotNull(resultado);
        assertEquals(plantaIdMock, resultado.getId());
        verify(plantaRepository).save(any(Planta.class));
    }

    //eliminar()
    @Test
    @DisplayName("Debe eliminar la planta")
    void eliminar() {
        when(plantaRepository.existsById(plantaIdMock)).thenReturn(true);

        plantaService.eliminar(plantaIdMock);

        verify(plantaRepository).deleteById(plantaIdMock);
    }

}