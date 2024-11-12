package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se proporciona una fecha no válida.
 *
 * Esta excepción es utilizada en situaciones donde una fecha ingresada es incorrecta o está
 * fuera de los rangos permitidos, por ejemplo, cuando una inscripción se realiza después de la fecha de la excursión.
 */
public class FechaInvalidaException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual la fecha es inválida.
     */
    public FechaInvalidaException(String message) {
        super(message);
    }
}
