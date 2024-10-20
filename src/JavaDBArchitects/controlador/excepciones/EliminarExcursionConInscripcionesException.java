package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta eliminar una excursión que tiene inscripciones activas.
 *
 * Esta excepción previene que una excursión sea eliminada si aún tiene socios inscritos,
 * lo cual garantiza que no se borren datos importantes.
 */
public class EliminarExcursionConInscripcionesException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual la excursión no puede ser eliminada.
     */
    public EliminarExcursionConInscripcionesException(String message) {
        super(message);
    }
}
