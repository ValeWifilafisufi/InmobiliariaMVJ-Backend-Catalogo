package cl.Inmobiliaria.catalogo_backend.controller;

import cl.Inmobiliaria.catalogo_backend.dto.PlantaRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PlantaResponse;
import cl.Inmobiliaria.catalogo_backend.service.PlantaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plantas")
@RequiredArgsConstructor
public class PlantaController {

    private final PlantaService plantaService;

    @GetMapping("/{id}")
    public ResponseEntity<PlantaResponse> obtenerPorId(@PathVariable Long id) {
        PlantaResponse response = plantaService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/propiedad/{propiedadId}")
    public ResponseEntity<PlantaResponse> agregarPlantaAProyecto(
            @PathVariable Long propiedadId,
            @Valid @RequestBody PlantaRequest request) {
        PlantaResponse nuevaPlanta = plantaService.agregarPlantaAProyecto(propiedadId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPlanta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlantaRequest request) {
        PlantaResponse actualizada = plantaService.actualizar(id, request);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        plantaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}