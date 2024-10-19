package JavaDBArchitects.modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListaExcursiones {
    private static List<Excursion> excursiones = new ArrayList<>();

    // Cargar datos iniciales (si es necesario)
    public static void cargarDatosExcursiones() {
        // Si necesitas cargar datos predeterminados, lo puedes hacer aquí
    }

    // Agregar una excursión a la lista
    public static void addExcursion(Excursion excursion) {
        excursiones.add(excursion);
    }

    // Comprobar si una excursión ya existe (por código de excursión)
    public static boolean excursionExiste(String codigoExcursion) {
        for (Excursion excursion : excursiones) {
            if (excursion.getCodigo().equals(codigoExcursion)) {
                return true;
            }
        }
        return false;
    }

    // Obtener todas las excursiones
    public static List<Excursion> getExcursiones() {
        return excursiones;
    }

    // Obtener una excursión específica (por código de excursión)
    public static Excursion getExcursion(String codigoExcursion) {
        for (Excursion excursion : excursiones) {
            if (excursion.getCodigo().equals(codigoExcursion)) {
                return excursion;
            }
        }
        return null;  // O puedes lanzar una excepción personalizada
    }

    // Eliminar una excursión
    public static boolean eliminarExcursion(String codigoExcursion) {
        for (Excursion excursion : excursiones) {
            if (excursion.getCodigo().equals(codigoExcursion)) {
                excursiones.remove(excursion);
                return true;
            }
        }
        return false;
    }

    // Listar todas las excursiones con fechas formateadas
    public static void listarExcursionesFormateadas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Excursion excursion : excursiones) {
            LocalDate fechaExcursion = excursion.getFecha().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            System.out.println("Excursión: " + excursion.getCodigo() +
                    ", Fecha: " + fechaExcursion.format(formatter) +
                    ", Descripción: " + excursion.getDescripcion());
        }
    }
}
