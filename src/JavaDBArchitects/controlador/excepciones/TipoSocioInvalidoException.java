package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta registrar o manipular un socio con un tipo no válido.
 *
 * Esta excepción es útil para manejar casos donde se provee un tipo de socio que no está definido
 * en el sistema, garantizando que solo se acepten tipos de socios válidos.
 */
public class TipoSocioInvalidoException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual el tipo de socio es inválido.
     */
    public TipoSocioInvalidoException(String message) {
        super(message);
    }
}
