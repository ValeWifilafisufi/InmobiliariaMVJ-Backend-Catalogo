package cl.Inmobiliaria.catalogo_backend.controller;

import cl.Inmobiliaria.catalogo_backend.config.SecurityConfig;
import cl.Inmobiliaria.catalogo_backend.dto.PlantaRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PlantaResponse;
import cl.Inmobiliaria.catalogo_backend.security.JwtAutenticacionFilter;
import cl.Inmobiliaria.catalogo_backend.service.PlantaService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = PlantaController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAutenticacionFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - PlantaController")
class PlantaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlantaService plantaService;

    private final Faker faker = new Faker(Locale.forLanguageTag("es-CL"));

    private PlantaResponse plantaResponseMock;
    private Long plantaIdMock;
    private Long proyectoIdMock;
    private String nombrePlantaMock;

    @BeforeEach
    void setUp() {
        plantaIdMock = 1L;
        proyectoIdMock = 10L;
        nombrePlantaMock = "Planta Tipo " + faker.letterify("?").toUpperCase();

        plantaResponseMock = PlantaResponse.builder()
                .id(plantaIdMock)
                .nombre(nombrePlantaMock)
                .precio(3500.0)
                .plano("/imagenes/plantaA.jpg")
                .dormitorios(2)
                .banos(2)
                .supInterior(58.0)
                .supTerraza(8.5)
                .supTotal(66.5)
                .disponible(true)
                .build();
    }

    // GET /api/plantas/{id}

    @Test
    @DisplayName("Debe retornar 200 OK y los datos de la planta cuando el ID existe")
    void obtenerPorIdRetorna200() throws Exception {
        when(plantaService.obtenerPorId(plantaIdMock)).thenReturn(plantaResponseMock);

        mockMvc.perform(get("/api/plantas/{id}", plantaIdMock))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(plantaIdMock))
                .andExpect(jsonPath("$.nombre").value(nombrePlantaMock))
                .andExpect(jsonPath("$.supTotal").value(66.5));
    }

    // POST /api/plantas/propiedad/{propiedadId}

    @Test
    @DisplayName("Debe retornar 201 Created al agregar una planta válida a un proyecto")
    void agregarPlantaAProyectoRetorna201() throws Exception {
        String jsonPlantaValida = """
                {
                  "nombre": "%s",
                  "precio": 3500.0,
                  "plano": "/imagenes/plantaA.jpg",
                  "dormitorios": 2,
                  "banos": 2,
                  "supInterior": 58.0,
                  "supTerraza": 8.5,
                  "disponible": true
                }
                """.formatted(nombrePlantaMock);

        when(plantaService.agregarPlantaAProyecto(eq(proyectoIdMock), any(PlantaRequest.class)))
                .thenReturn(plantaResponseMock);

        mockMvc.perform(post("/api/plantas/propiedad/{propiedadId}", proyectoIdMock)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPlantaValida))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(plantaIdMock))
                .andExpect(jsonPath("$.nombre").value(nombrePlantaMock));

        verify(plantaService).agregarPlantaAProyecto(eq(proyectoIdMock), any(PlantaRequest.class));
    }

    @Test
    @DisplayName("Debe retornar 400 Bad Request cuando los datos de la planta no pasan @Valid")
    void agregarInvalidoRetorna400() throws Exception {
        String jsonPlantaInvalida = """
                {
                  "nombre": "",
                  "precio": -100.0,
                  "dormitorios": 0,
                  "banos": 0,
                  "supInterior": -10.0
                }
                """;

        mockMvc.perform(post("/api/plantas/propiedad/{propiedadId}", proyectoIdMock)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPlantaInvalida))
                .andExpect(status().isBadRequest());

        verify(plantaService, never()).agregarPlantaAProyecto(any(), any());
    }

    // PUT /api/plantas/{id}

    @Test
    @DisplayName("Debe retornar 200 OK al actualizar una planta existente")
    void actualizarRetorna200() throws Exception {
        String jsonActualizar = """
                {
                  "nombre": "%s",
                  "precio": 3650.0,
                  "plano": "/imagenes/plantaA.jpg",
                  "dormitorios": 2,
                  "banos": 2,
                  "supInterior": 58.0,
                  "supTerraza": 8.5,
                  "disponible": true
                }
                """.formatted(nombrePlantaMock);

        when(plantaService.actualizar(eq(plantaIdMock), any(PlantaRequest.class)))
                .thenReturn(plantaResponseMock);

        mockMvc.perform(put("/api/plantas/{id}", plantaIdMock)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonActualizar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(plantaIdMock));

        verify(plantaService).actualizar(eq(plantaIdMock), any(PlantaRequest.class));
    }

    // DELETE /api/plantas/{id}

    @Test
    @DisplayName("Debe retornar 204 No Content al eliminar una planta")
    void eliminar_Exito_Retorna204() throws Exception {
        doNothing().when(plantaService).eliminar(plantaIdMock);

        mockMvc.perform(delete("/api/plantas/{id}", plantaIdMock))
                .andExpect(status().isNoContent());

        verify(plantaService).eliminar(plantaIdMock);
    }
}