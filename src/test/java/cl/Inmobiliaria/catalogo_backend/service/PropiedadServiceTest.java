package cl.Inmobiliaria.catalogo_backend.service;

import cl.Inmobiliaria.catalogo_backend.dto.PlantaRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PropiedadRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PropiedadResponse;
import cl.Inmobiliaria.catalogo_backend.entity.Propiedad;
import cl.Inmobiliaria.catalogo_backend.repository.PropiedadRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - PropiedadService")
class PropiedadServiceTest {

    @Mock
    private PropiedadRepository repositorio;

    @InjectMocks
    private PropiedadService propiedadService;

    private final Faker faker = new Faker(Locale.forLanguageTag("es-CL"));

    private Propiedad propiedadMock;
    private Propiedad proyectoMock;
    private PropiedadRequest propiedadRequestMock;
    private PropiedadRequest proyectoRequestMock;
    private Long idPropiedadMock;
    private Long idProyectoMock;

    @BeforeEach
    void setUp() {
        idPropiedadMock = 1L;
        idProyectoMock = 2L;

        String tituloCasa = "Casa " + faker.address().streetName();
        String ubicacionCasa = faker.address().cityName();

        propiedadMock = Propiedad.builder()
                .id(idPropiedadMock)
                .esProyecto(false)
                .tipo("Casa")
                .titulo(tituloCasa)
                .ubicacion(ubicacionCasa)
                .precio(2800.0)
                .descripcion("Calidad exclusiva en el corazón de Santiago")
                .imagenes(List.of("/imagenes/casaFam1.jpg"))
                .destacada(true)
                .disponible(true)
                .dormitorios(3)
                .banos(2)
                .metros(95.0)
                .plantas(List.of())
                .build();

        proyectoMock = Propiedad.builder()
                .id(idProyectoMock)
                .esProyecto(true)
                .tipo("Condominio")
                .titulo("Condominio " + faker.company().name())
                .ubicacion("Quilpué")
                .precio(4200.0)
                .fechaEntrega("Entrega Segundo Semestre 2027")
                .descripcion("Exclusivo condominio con amplios jardines y áreas comunes en Quilpué.")
                .imagenes(List.of("/imagenes/proyecto1.jpg"))
                .destacada(false)
                .disponible(true)
                .plantas(List.of())
                .build();

        propiedadRequestMock = new PropiedadRequest(
                false,
                "Casa",
                tituloCasa,
                ubicacionCasa,
                2800.0,
                null,
                propiedadMock.getDescripcion(),
                List.of("/imagenes/casaFam1.jpg"),
                true,
                true,
                3,
                2,
                95.0,
                List.of()
        );

        proyectoRequestMock = new PropiedadRequest(
                true,
                "Condominio",
                proyectoMock.getTitulo(),
                "Quilpué",
                4200.0,
                "Entrega Segundo Semestre 2027",
                proyectoMock.getDescripcion(),
                List.of("/imagenes/proyecto1.jpg"),
                false,
                true,
                null,
                null,
                null,
                List.of()
        );
    }

    //listar()

    @Test
    @DisplayName("Debe retornar todas las publicaciones cuando no se envían filtros")
    void listarSinFiltro() {
        when(repositorio.findAll()).thenReturn(List.of(propiedadMock, proyectoMock));

        List<PropiedadResponse> resultado = propiedadService.listar(null, null, null, null, null, null, null);

        assertEquals(2, resultado.size());
        verify(repositorio).findAll();
    }

    @Test
    @DisplayName("Debe filtrar correctamente por tipo, ubicación, rango de precio, dormitorios, baños y rango de metros")
    void listarConFiltros() {
        when(repositorio.findAll()).thenReturn(List.of(propiedadMock, proyectoMock));

        List<PropiedadResponse> resultado = propiedadService.listar(
                "Casa",
                propiedadMock.getUbicacion(),
                "2500-3000",
                false,
                3,
                2,
                "50-100"
        );

        assertEquals(1, resultado.size());
        assertEquals(idPropiedadMock, resultado.get(0).getId());
        assertEquals("Casa", resultado.get(0).getTipo());
    }

    @Test
    @DisplayName("Debe filtrar únicamente proyectos inmobiliarios cuando esProyecto es true")
    void listarSoloProyectos() {
        when(repositorio.findAll()).thenReturn(List.of(propiedadMock, proyectoMock));

        List<PropiedadResponse> resultado = propiedadService.listar(
                null, null, null, true, null, null, null
        );

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isEsProyecto());
        assertEquals(idProyectoMock, resultado.get(0).getId());
    }

    //obtenerPorId()

    @Test
    @DisplayName("Debe retornar la propiedad al buscarla por ID")
    void obtenerPorId() {
        when(repositorio.findById(idPropiedadMock)).thenReturn(Optional.of(propiedadMock));

        PropiedadResponse resultado = propiedadService.obtenerPorId(idPropiedadMock);

        assertNotNull(resultado);
        assertEquals(idPropiedadMock, resultado.getId());
        assertEquals(propiedadMock.getTitulo(), resultado.getTitulo());
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el ID de la propiedad no existe")
    void IdNoEncontrado() {
        when(repositorio.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> propiedadService.obtenerPorId(999L));
    }

    //crear()

    @Test
    @DisplayName("Debe crear una propiedad")
    void crearPropiedad() {
        when(repositorio.save(any(Propiedad.class))).thenAnswer(inv -> {
            Propiedad p = inv.getArgument(0);
            p.setId(10L);
            return p;
        });

        PropiedadResponse resultado = propiedadService.crear(propiedadRequestMock);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals(propiedadRequestMock.titulo(), resultado.getTitulo());
        assertEquals("Casa", resultado.getTipo());
        verify(repositorio).save(any(Propiedad.class));
    }

    @Test
    @DisplayName("Debe crear un proyecto inmobiliario con sus plantas")
    void crearroyecto() {
        PlantaRequest plantaReq = new PlantaRequest(
                null, "Modelo A", 3800.0, "/imagenes/plantaA.jpg",
                2, 2, 60.5, 10.2, null, true
        );

        PropiedadRequest reqProyectoConPlanta = new PropiedadRequest(
                true, "Condominio", "Torre Mirador", "Concón",
                3800.0, "Entrega 2027", "Vista al mar",
                List.of("/imagenes/proyecto1.jpg"), false, true,
                null, null, null, List.of(plantaReq)
        );

        when(repositorio.save(any(Propiedad.class))).thenAnswer(inv -> {
            Propiedad p = inv.getArgument(0);
            p.setId(20L);
            return p;
        });

        PropiedadResponse resultado = propiedadService.crear(reqProyectoConPlanta);

        assertNotNull(resultado);
        assertTrue(resultado.isEsProyecto());
        assertEquals(1, resultado.getPlantas().size());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando un proyecto no tiene fecha de entrega")
    void FallaProyectoSinFechaEntrega() {
        PropiedadRequest reqInvalido = new PropiedadRequest(
                true, "Condominio", "Proyecto Sin Fecha", "Quilpué",
                4000.0, "   ", "Descripción",
                List.of(), false, true, null, null, null, List.of()
        );

        assertThrows(IllegalArgumentException.class, () -> propiedadService.crear(reqInvalido));
        verify(repositorio, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando una propiedad individual tiene dormitorios, baños o metros inválidos")
    void FallaPropiedadIndividualDatosIncompletos() {
        PropiedadRequest sinDormitorios = new PropiedadRequest(
                false, "Casa", "Casa Sin Dorms", "Quilpué",
                2500.0, null, "Descripción",
                List.of(), false, true, 0, 2, 80.0, List.of()
        );

        PropiedadRequest sinBanos = new PropiedadRequest(
                false, "Casa", "Casa Sin Baños", "Quilpué",
                2500.0, null, "Descripción",
                List.of(), false, true, 2, null, 80.0, List.of()
        );

        PropiedadRequest sinMetros = new PropiedadRequest(
                false, "Casa", "Casa Sin Metros", "Quilpué",
                2500.0, null, "Descripción",
                List.of(), false, true, 2, 1, 0.0, List.of()
        );

        assertThrows(IllegalArgumentException.class, () -> propiedadService.crear(sinDormitorios));
        assertThrows(IllegalArgumentException.class, () -> propiedadService.crear(sinBanos));
        assertThrows(IllegalArgumentException.class, () -> propiedadService.crear(sinMetros));
        verify(repositorio, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando se supera el límite de 4 destacadas")
    void FallaPorLimiteDeDestacadas() {
        when(repositorio.countByEsProyectoAndDestacadaTrueAndDisponibleTrue(false)).thenReturn(4L);

        assertThrows(IllegalArgumentException.class, () -> propiedadService.crear(propiedadRequestMock));
        verify(repositorio, never()).save(any());
    }

    //actualizar()

    @Test
    @DisplayName("Debe actualizar la propiedad existente")
    void actualizarPropiedad() {
        when(repositorio.existsById(idPropiedadMock)).thenReturn(true);
        when(repositorio.countByEsProyectoAndDestacadaTrueAndDisponibleTrueAndIdNot(false, idPropiedadMock))
                .thenReturn(2L);
        when(repositorio.save(any(Propiedad.class))).thenReturn(propiedadMock);

        PropiedadResponse resultado = propiedadService.actualizar(idPropiedadMock, propiedadRequestMock);

        assertNotNull(resultado);
        assertEquals(idPropiedadMock, resultado.getId());
        verify(repositorio).save(any(Propiedad.class));
    }

    @Test
    @DisplayName("Debe actualizar un proyecto inmobiliario")
    void actualizarProyecto() {
        when(repositorio.existsById(idProyectoMock)).thenReturn(true);
        when(repositorio.save(any(Propiedad.class))).thenReturn(proyectoMock);

        PropiedadResponse resultado = propiedadService.actualizar(idProyectoMock, proyectoRequestMock);

        assertNotNull(resultado);
        assertEquals(idProyectoMock, resultado.getId());
        assertTrue(resultado.isEsProyecto());
        verify(repositorio).save(any(Propiedad.class));
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException al intentar actualizar una propiedad inexistente")
    void actualizarNoEncontrada() {
        when(repositorio.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> propiedadService.actualizar(999L, propiedadRequestMock));
        verify(repositorio, never()).save(any());
    }

    //eliminar()

    @Test
    @DisplayName("Debe eliminar la propiedad")
    void eliminar() {
        when(repositorio.existsById(idPropiedadMock)).thenReturn(true);

        propiedadService.eliminar(idPropiedadMock);

        verify(repositorio).deleteById(idPropiedadMock);
    }

}