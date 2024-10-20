package JavaDBArchitects.modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class ListaInscripciones {

    // Lista para almacenar todas las inscripciones
    private static List<Inscripcion> inscripciones = new ArrayList<>();

    // Contador para generar números únicos de inscripciones
    private static int contadorInscripciones = 0;

    /**
     * Cargar datos iniciales de inscripciones si es necesario.
     * Se puede usar para cargar inscripciones predefinidas.
     */
    public static void cargarDatosInscripciones() {
        // Si necesitáramos cargar datos predeterminados, lo podemos hacer aquí
    }

    /**
     * Genera un número único para la inscripción.
     *
     * @return El número de inscripción generado.
     */
    public static int generarNumeroInscripcion() {
        return ++contadorInscripciones;
    }

    /**
     * Agrega una inscripción a la lista.
     *
     * @param inscripcion La inscripción que se añadirá a la lista.
     */
    public static void addInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
    }

    /**
     * Comprueba si una inscripción existe por su número.
     *
     * @param numInscripcion El número de la inscripción.
     * @return true si la inscripción existe, false si no.
     */
    public static boolean inscripcionExiste(String numInscripcion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getNumInscripcion().equals(numInscripcion)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprueba si una inscripción existe por el número de socio y el código de excursión.
     *
     * @param numeroSocio El número del socio.
     * @param codigoExcursion El código de la excursión.
     * @return true si la inscripción existe, false si no.
     */
    public static boolean inscripcionExiste(String numeroSocio, String codigoExcursion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getSocio().getNumeroSocio().equals(numeroSocio) &&
                    inscripcion.getExcursion().getCodigo().equals(codigoExcursion)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina una inscripción por su número.
     *
     * @param numInscripcion El número de la inscripción a eliminar.
     * @return true si se elimina correctamente, false si no se encuentra.
     */
    public static boolean eliminarInscripcion(String numInscripcion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getNumInscripcion().equals(numInscripcion)) {
                inscripciones.remove(inscripcion);
                return true;
            }
        }
        return false;
    }

    /**
     * Devuelve todas las inscripciones registradas.
     *
     * @return La lista de inscripciones.
     */
    public static List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    /**
     * Devuelve una inscripción específica por su número.
     *
     * @param numInscripcion El número de la inscripción.
     * @return La inscripción encontrada, o null si no existe.
     */
    public static Inscripcion getInscripcion(String numInscripcion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getNumInscripcion().equals(numInscripcion)) {
                return inscripcion;
            }
        }
        return null;  // También se podría lanzar una excepción personalizada aquí
    }

    /**
     * Comprueba si una excursión tiene inscripciones activas.
     *
     * @param excursion La excursión a verificar.
     * @return true si la excursión tiene inscripciones, false si no.
     */
    public static boolean excursionTieneInscripciones(Excursion excursion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getExcursion().equals(excursion)) {
                return true;  // Si encontramos una inscripción para esa excursión
            }
        }
        return false;  // No hay inscripciones para esta excursión
    }

    /**
     * Comprueba si un socio tiene inscripciones activas.
     *
     * @param numeroSocio El número del socio.
     * @return true si el socio tiene inscripciones activas, false si no.
     */
    public static boolean socioTieneInscripciones(String numeroSocio) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getSocio().getNumeroSocio().equals(numeroSocio)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lista todas las inscripciones mostrando las fechas formateadas.
     */
    public static void listarInscripcionesFormateadas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Inscripcion inscripcion : inscripciones) {
            LocalDate fechaInscripcion = inscripcion.getFechaInscripcion().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            System.out.println("Inscripción: " + inscripcion.getNumInscripcion() +
                    ", Fecha de inscripción: " + fechaInscripcion.format(formatter));
        }
    }
}

