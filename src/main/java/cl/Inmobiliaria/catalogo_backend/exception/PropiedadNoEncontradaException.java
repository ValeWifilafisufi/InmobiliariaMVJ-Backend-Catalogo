package cl.Inmobiliaria.catalogo_backend.exception;

public class PropiedadNoEncontradaException extends RuntimeException {
    public PropiedadNoEncontradaException(Long id) {
        super("No existe la propiedad con ID " + id + ".");
    }
}
