package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta registrar una inscripción que ya existe.
 *
 * Esta excepción se utiliza para evitar duplicados de inscripciones, garantizando que
 * un socio no se inscriba más de una vez en la misma excursión.
 */
public class InscripcionYaExisteException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual la inscripción ya existe.
     */
    public InscripcionYaExisteException(String message) {
        super(message);
    }
}
