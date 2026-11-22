package cl.Inmobiliaria.catalogo_backend.controller;

import cl.Inmobiliaria.catalogo_backend.config.SecurityConfig;
import cl.Inmobiliaria.catalogo_backend.dto.PropiedadRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PropiedadResponse;
import cl.Inmobiliaria.catalogo_backend.security.JwtAutenticacionFilter;
import cl.Inmobiliaria.catalogo_backend.service.PropiedadService;
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

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = PropiedadController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAutenticacionFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - PropiedadController")
class PropiedadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PropiedadService propiedadService;

    private final Faker faker = new Faker(Locale.forLanguageTag("es-CL"));

    private PropiedadResponse propiedadResponseMock;
    private Long idMock;
    private String tituloMock;
    private String ubicacionMock;

    @BeforeEach
    void setUp() {
        idMock = 1L;
        tituloMock = "Casa " + faker.address().streetName();
        ubicacionMock = "Quilpué";

        propiedadResponseMock = PropiedadResponse.builder()
                .id(idMock)
                .esProyecto(false)
                .tipo("Casa")
                .titulo(tituloMock)
                .ubicacion(ubicacionMock)
                .precio(3200.0)
                .descripcion("Amplia casa familiar con patio")
                .imagenes(List.of("/imagenes/casaFam1.jpg"))
                .destacada(true)
                .disponible(true)
                .dormitorios(3)
                .banos(2)
                .metros(110.0)
                .plantas(List.of())
                .build();
    }

    // GET /api/propiedades

    @Test
    @DisplayName("Debe retornar 200 OK y la lista completa cuando no se envían filtros")
    void listarRetorna200() throws Exception {
        when(propiedadService.listar(null, null, null, null, null, null, null))
                .thenReturn(List.of(propiedadResponseMock));

        mockMvc.perform(get("/api/propiedades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(idMock))
                .andExpect(jsonPath("$[0].titulo").value(tituloMock));

        verify(propiedadService).listar(null, null, null, null, null, null, null);
    }

    @Test
    @DisplayName("Debe retornar 200 OK y reenviar los parámetros de filtro al servicio")
    void listarConFiltrosRetorna200() throws Exception {
        when(propiedadService.listar("Casa", "Quilpué", "3000-4000", false, 3, 2, "100-150"))
                .thenReturn(List.of(propiedadResponseMock));

        mockMvc.perform(get("/api/propiedades")
                        .param("tipo", "Casa")
                        .param("ubicacion", "Quilpué")
                        .param("precio", "3000-4000")
                        .param("esProyecto", "false")
                        .param("dormitorios", "3")
                        .param("banos", "2")
                        .param("metros", "100-150"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ubicacion").value("Quilpué"));
    }

    // GET /api/propiedades/{id}

    @Test
    @DisplayName("Debe retornar 200 OK y el detalle de la propiedad cuando el ID existe")
    void obtenerPorIdRetorna200() throws Exception {
        when(propiedadService.obtenerPorId(idMock)).thenReturn(propiedadResponseMock);

        mockMvc.perform(get("/api/propiedades/{id}", idMock))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idMock))
                .andExpect(jsonPath("$.titulo").value(tituloMock))
                .andExpect(jsonPath("$.precio").value(3200.0));
    }

    // POST /api/propiedades

    @Test
    @DisplayName("Debe retornar 201 Created cuando el cuerpo JSON es válido")
    void crearRetorna201() throws Exception {
        String jsonValido = """
                {
                  "esProyecto": false,
                  "tipo": "Casa",
                  "titulo": "%s",
                  "ubicacion": "Quilpué",
                  "precio": 3200.0,
                  "descripcion": "Amplia casa familiar con patio",
                  "imagenes": ["/imagenes/casaFam1.jpg"],
                  "destacada": true,
                  "disponible": true,
                  "dormitorios": 3,
                  "banos": 2,
                  "metros": 110.0,
                  "plantas": []
                }
                """.formatted(tituloMock);

        when(propiedadService.crear(any(PropiedadRequest.class))).thenReturn(propiedadResponseMock);

        mockMvc.perform(post("/api/propiedades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(idMock))
                .andExpect(jsonPath("$.titulo").value(tituloMock));

        verify(propiedadService).crear(any(PropiedadRequest.class));
    }

    @Test
    @DisplayName("Debe retornar 400 Bad Request cuando @Valid detecta campos obligatorios vacíos o precio negativo")
    void crearInvalidoRetorna400() throws Exception {
        String jsonInvalido = """
                {
                  "esProyecto": false,
                  "tipo": "",
                  "titulo": "",
                  "ubicacion": "",
                  "precio": -500.0,
                  "descripcion": ""
                }
                """;

        mockMvc.perform(post("/api/propiedades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest());

        verify(propiedadService, never()).crear(any());
    }

    // PUT /api/propiedades/{id}

    @Test
    @DisplayName("Debe retornar 200 OK al actualizar una propiedad con datos válidos")
    void actualizarRetorna200() throws Exception {
        String jsonActualizar = """
                {
                  "esProyecto": false,
                  "tipo": "Casa",
                  "titulo": "%s",
                  "ubicacion": "Quilpué",
                  "precio": 3400.0,
                  "descripcion": "Casa remodelada",
                  "imagenes": ["/imagenes/casaFam1.jpg"],
                  "destacada": false,
                  "disponible": true,
                  "dormitorios": 3,
                  "banos": 2,
                  "metros": 110.0,
                  "plantas": []
                }
                """.formatted(tituloMock);

        when(propiedadService.actualizar(eq(idMock), any(PropiedadRequest.class)))
                .thenReturn(propiedadResponseMock);

        mockMvc.perform(put("/api/propiedades/{id}", idMock)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonActualizar))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idMock));

        verify(propiedadService).actualizar(eq(idMock), any(PropiedadRequest.class));
    }

    // DELETE /api/propiedades/{id}

    @Test
    @DisplayName("Debe retornar 204 No Content al eliminar una propiedad existente")
    void eliminarRetorna204() throws Exception {
        doNothing().when(propiedadService).eliminar(idMock);

        mockMvc.perform(delete("/api/propiedades/{id}", idMock))
                .andExpect(status().isNoContent());

        verify(propiedadService).eliminar(idMock);
    }
}