package cl.Inmobiliaria.catalogo_backend.controller;

import cl.Inmobiliaria.catalogo_backend.dto.PropiedadRequest;
import cl.Inmobiliaria.catalogo_backend.dto.PropiedadResponse;
import cl.Inmobiliaria.catalogo_backend.service.PropiedadService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<PropiedadResponse>> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String precio,
            @RequestParam(required = false) Boolean esProyecto,
            @RequestParam(required = false) Integer dormitorios,
            @RequestParam(required = false) Integer banos,
            @RequestParam(required = false) String metros) {

        List<PropiedadResponse> lista = propiedadService.listar(
                tipo, ubicacion, precio, esProyecto, dormitorios, banos, metros);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadResponse> obtenerPorId(@PathVariable Long id) {
        PropiedadResponse response = propiedadService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PropiedadResponse> crear(@Valid @RequestBody PropiedadRequest request) {
        PropiedadResponse creada = propiedadService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropiedadResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PropiedadRequest request) {
        PropiedadResponse actualizada = propiedadService.actualizar(id, request);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        propiedadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}