package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta acceder o manipular un socio que no existe.
 *
 * Esta excepción se utiliza para manejar casos en los que se busca, modifica o elimina un socio
 * que no está registrado en el sistema.
 */
public class SocioNoExisteException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual el socio no existe.
     */
    public SocioNoExisteException(String message) {
        super(message);
    }
}
