package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta registrar una excursión que ya existe en el sistema.
 *
 * Esta excepción es utilizada para evitar duplicar excursiones, garantizando que cada excursión tenga
 * un código único en el sistema.
 */
public class ExcursionYaExisteException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual la excursión ya existe.
     */
    public ExcursionYaExisteException(String message) {
        super(message);
    }
}
