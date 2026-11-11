package cl.Inmobiliaria.catalogo_backend.controller;

import cl.Inmobiliaria.catalogo_backend.dto.PlantaDTO;
import cl.Inmobiliaria.catalogo_backend.service.PlantaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plantas")
@RequiredArgsConstructor
public class PlantaController {

    private final PlantaService plantaService;

    // GET /api/plantas/1
    @GetMapping("/{id}")
    public ResponseEntity<PlantaDTO> obtenerPorId(@PathVariable Long id) {
        PlantaDTO dto = plantaService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    // POST /api/plantas/propiedad/9 (Asocia una nueva planta al proyecto con ID 9)
    @PostMapping("/propiedad/{propiedadId}")
    public ResponseEntity<PlantaDTO> agregarPlantaAProyecto(
            @PathVariable Long propiedadId, 
            @RequestBody PlantaDTO dto) {
        
        PlantaDTO nuevaPlanta = plantaService.agregarPlantaAProyecto(propiedadId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPlanta);
    }

    // PUT /api/plantas/1
    @PutMapping("/{id}")
    public ResponseEntity<PlantaDTO> actualizar(@PathVariable Long id, @RequestBody PlantaDTO dto) {
        PlantaDTO actualizada = plantaService.actualizar(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // DELETE /api/plantas/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        plantaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
