package cl.Inmobiliaria.catalogo_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record PropiedadRequest(
        boolean esProyecto,

        @NotBlank(message = "El tipo de inmueble es obligatorio")
        String tipo,

        @NotBlank(message = "El título es obligatorio")
        String titulo,

        @NotBlank(message = "La ubicación es obligatoria")
        String ubicacion,

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser mayor a 0")
        Double precio,

        String fechaEntrega,

        @NotBlank(message = "La descripción es obligatoria")
        String descripcion,

        List<String> imagenes,
        boolean destacada,
        boolean disponible,
        Integer dormitorios,
        Integer banos,
        Double metros,

        @Valid
        List<PlantaRequest> plantas
) {}