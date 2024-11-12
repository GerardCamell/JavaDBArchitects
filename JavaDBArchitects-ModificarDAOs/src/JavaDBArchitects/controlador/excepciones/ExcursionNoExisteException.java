package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta acceder o manipular una excursión que no existe.
 *
 * Esta excepción es utilizada para manejar casos en los que se busca o elimina una excursión que
 * no se encuentra registrada en el sistema.
 */
public class ExcursionNoExisteException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual la excursión no existe.
     */
    public ExcursionNoExisteException(String message) {
        super(message);
    }
}
