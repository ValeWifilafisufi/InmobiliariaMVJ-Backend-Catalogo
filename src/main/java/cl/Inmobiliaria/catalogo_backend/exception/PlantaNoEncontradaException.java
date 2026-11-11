package cl.Inmobiliaria.catalogo_backend.exception;

public class PlantaNoEncontradaException extends RuntimeException {
    public PlantaNoEncontradaException(Long id) {
        super("No existe la planta tipología con ID " + id + ".");
    }
}