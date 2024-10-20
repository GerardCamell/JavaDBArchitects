package JavaDBArchitects.modelo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListaExcursiones {
    // Lista estática que almacena todas las excursiones registradas
    private static List<Excursion> excursiones = new ArrayList<>();

    /**
     * Método para cargar datos iniciales de excursiones si es necesario.
     * Actualmente no implementado, pero puede utilizarse para cargar excursiones predeterminadas.
     */
    public static void cargarDatosExcursiones() {
        // Si necesitas cargar datos predeterminados, lo puedes hacer aquí
    }

    /**
     * Método que agrega una excursión a la lista de excursiones.
     *
     * @param excursion Objeto Excursion que se desea agregar.
     */
    public static void addExcursion(Excursion excursion) {
        excursiones.add(excursion);
    }

    /**
     * Método que verifica si una excursión ya existe en la lista, según su código.
     *
     * @param codigoExcursion Código de la excursión a verificar.
     * @return true si la excursión ya existe, false en caso contrario.
     */
    public static boolean excursionExiste(String codigoExcursion) {
        for (Excursion excursion : excursiones) {
            if (excursion.getCodigo().equals(codigoExcursion)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método que devuelve la lista completa de excursiones registradas.
     *
     * @return Lista de excursiones.
     */
    public static List<Excursion> getExcursiones() {
        return excursiones;
    }

    /**
     * Método que devuelve una excursión específica según su código.
     *
     * @param codigoExcursion Código de la excursión a buscar.
     * @return La excursión si se encuentra, null en caso contrario.
     */
    public static Excursion getExcursion(String codigoExcursion) {
        for (Excursion excursion : excursiones) {
            if (excursion.getCodigo().equals(codigoExcursion)) {
                return excursion;
            }
        }
        return null;  // O puedes lanzar una excepción personalizada
    }

    /**
     * Método que elimina una excursión de la lista según su código.
     *
     * @param codigoExcursion Código de la excursión a eliminar.
     * @return true si la excursión se eliminó con éxito, false si no se encontró.
     */
    public static boolean eliminarExcursion(String codigoExcursion) {
        for (Excursion excursion : excursiones) {
            if (excursion.getCodigo().equals(codigoExcursion)) {
                excursiones.remove(excursion);
                return true;
            }
        }
        return false;
    }

    /**
     * Método que imprime todas las excursiones en la consola con las fechas formateadas.
     * Se utiliza para mostrar una lista formateada de todas las excursiones registradas.
     */
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

