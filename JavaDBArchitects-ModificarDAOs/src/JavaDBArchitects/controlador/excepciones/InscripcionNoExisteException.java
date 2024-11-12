package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta acceder o eliminar una inscripción que no existe.
 *
 * Esta excepción es útil para evitar errores en el manejo de inscripciones inexistentes,
 * como cuando se intenta eliminar una inscripción no registrada o consultar una que no está en el sistema.
 */
public class InscripcionNoExisteException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual la inscripción no existe.
     */
    public InscripcionNoExisteException(String message) {
        super(message);
    }
}
