package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta realizar una cancelación no válida.
 *
 * Se utiliza principalmente para evitar eliminar inscripciones de excursiones
 * que ya han sido realizadas o cuando no se cumplen los requisitos necesarios
 * para una cancelación.
 */
public class CancelacionInvalidaException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo de la cancelación inválida.
     *
     * @param message Mensaje que describe el error.
     */
    public CancelacionInvalidaException(String message) {
        super(message);
    }
}
