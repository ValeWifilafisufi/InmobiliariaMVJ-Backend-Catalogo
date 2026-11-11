package cl.Inmobiliaria.catalogo_backend.controller;

import cl.Inmobiliaria.catalogo_backend.dto.PropiedadDTO;
import cl.Inmobiliaria.catalogo_backend.service.PropiedadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propiedades")
@RequiredArgsConstructor
public class PropiedadController {

    private final PropiedadService propiedadService;

    @GetMapping
    public ResponseEntity<List<PropiedadDTO>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String precio,
            @RequestParam(required = false) Boolean esProyecto,
            @RequestParam(required = false) Integer dormitorios,
            @RequestParam(required = false) Integer banos,
            @RequestParam(required = false) String metros) {
        
        List<PropiedadDTO> lista = propiedadService.listar(tipo, ubicacion, precio, esProyecto, dormitorios, banos, metros);
        return ResponseEntity.ok(lista);
    } // <--- Faltaba esta llave de cierre

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadDTO> obtenerPorId(@PathVariable Long id) {
        PropiedadDTO dto = propiedadService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PropiedadDTO> crear(@RequestBody PropiedadDTO dto) {
        PropiedadDTO creada = propiedadService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropiedadDTO> actualizar(@PathVariable Long id, @RequestBody PropiedadDTO dto) {
        PropiedadDTO actualizada = propiedadService.actualizar(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        propiedadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}