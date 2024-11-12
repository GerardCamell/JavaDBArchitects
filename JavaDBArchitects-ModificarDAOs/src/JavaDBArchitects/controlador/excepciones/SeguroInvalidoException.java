package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se proporciona un seguro no válido para un socio.
 *
 * Esta excepción se utiliza para manejar casos en los que el seguro proporcionado
 * para un socio estándar no cumple con los requisitos necesarios o es incorrecto.
 */
public class SeguroInvalidoException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual el seguro es inválido.
     */
    public SeguroInvalidoException(String message) {
        super(message);
    }
}
