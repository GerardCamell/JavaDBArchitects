package JavaDBArchitects.controlador.excepciones;

/**
 * Excepción que se lanza cuando se intenta eliminar un socio que tiene inscripciones activas.
 *
 * Esta excepción previene la eliminación de socios que aún tienen inscripciones a excursiones,
 * lo que asegura que no se borren datos importantes antes de cancelar o finalizar dichas inscripciones.
 */
public class SocioConInscripcionesException extends Exception {

    /**
     * Constructor que recibe un mensaje que describe el motivo del error.
     *
     * @param message Mensaje que describe la razón por la cual el socio no puede ser eliminado.
     */
    public SocioConInscripcionesException(String message) {
        super(message);
    }
}
