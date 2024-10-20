package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta registrar un socio que ya existe en el sistema.
 *
 * Esta excepción se utiliza para evitar duplicados en el registro de socios, asegurando que cada socio
 * tenga un número único en el sistema.
 */
public class SocioYaExisteException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual el socio ya existe.
     */
    public SocioYaExisteException(String message) {
        super(message);
    }
}
