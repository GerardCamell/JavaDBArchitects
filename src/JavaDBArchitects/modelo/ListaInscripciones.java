package JavaDBArchitects.modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class ListaInscripciones {
    private static List<Inscripcion> inscripciones = new ArrayList<>();

    // Cargar datos iniciales (si es necesario)
    public static void cargarDatosInscripciones() {
        // Si necesitas cargar datos predeterminados, lo puedes hacer aquí
    }

    // Agregar una inscripción a la lista
    public static void addInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
    }

    // Comprobar si una inscripción ya existe (por número de inscripción)
    public static boolean inscripcionExiste(String numInscripcion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getNumInscripcion().equals(numInscripcion)) {
                return true;
            }
        }
        return false;
    }

    // Comprobar si una inscripción ya existe (por número de socio y código de excursión)
    public static boolean inscripcionExiste(String numeroSocio, String codigoExcursion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getSocio().getNumeroSocio().equals(numeroSocio) &&
                    inscripcion.getExcursion().getCodigo().equals(codigoExcursion)) {
                return true;
            }
        }
        return false;
    }

    // Eliminar una inscripción
    public static boolean eliminarInscripcion(String numInscripcion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getNumInscripcion().equals(numInscripcion)) {
                inscripciones.remove(inscripcion);
                return true;
            }
        }
        return false;
    }

    // Obtener todas las inscripciones
    public static List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    // Obtener una inscripción específica (por número de inscripción)
    public static Inscripcion getInscripcion(String numInscripcion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getNumInscripcion().equals(numInscripcion)) {
                return inscripcion;
            }
        }
        return null;  // O puedes lanzar una excepción personalizada
    }

    // Comprobar si una excursión tiene inscripciones
    public static boolean excursionTieneInscripciones(Excursion excursion) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getExcursion().equals(excursion)) {
                return true;  // Si encontramos una inscripción para esa excursión
            }
        }
        return false;  // No hay inscripciones para esta excursión
    }

    // Comprobar si un socio tiene inscripciones activas
    public static boolean socioTieneInscripciones(String numeroSocio) {
        for (Inscripcion inscripcion : inscripciones) {
            if (inscripcion.getSocio().getNumeroSocio().equals(numeroSocio)) {
                return true;
            }
        }
        return false;
    }

    // Método para listar inscripciones con fechas formateadas
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
