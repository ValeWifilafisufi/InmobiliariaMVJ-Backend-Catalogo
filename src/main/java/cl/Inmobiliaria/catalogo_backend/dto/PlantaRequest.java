package cl.Inmobiliaria.catalogo_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlantaRequest(
        Long id,

        @NotBlank(message = "El nombre de la planta es obligatorio")
        String nombre,

        @NotNull(message = "El precio de la planta es obligatorio")
        @Positive(message = "El precio debe ser mayor a 0")
        Double precio,

        String plano,

        @NotNull(message = "La cantidad de dormitorios es obligatoria")
        @Min(value = 1, message = "Debe tener al menos 1 dormitorio")
        Integer dormitorios,

        @NotNull(message = "La cantidad de baños es obligatoria")
        @Min(value = 1, message = "Debe tener al menos 1 baño")
        Integer banos,

        @NotNull(message = "La superficie interior es obligatoria")
        @Positive(message = "La superficie interior debe ser mayor a 0")
        Double supInterior,

        Double supTerraza,
        Double supTotal,
        Boolean disponible
) {}