package JavaDBArchitects.modelo;

import java.util.ArrayList;
import java.util.List;

public class ListaSocios {
    private static List<Socio> socios = new ArrayList<>();

    // Cargar datos iniciales (si es necesario)
    public static void cargarDatosSocios() {
        // Si necesitas cargar datos predeterminados, lo puedes hacer aquí
    }

    // Agregar un socio a la lista
    public static void addSocio(Socio socio) {
        socios.add(socio);
    }

    // Comprobar si un socio ya existe (por número de socio)
    public static boolean socioExiste(String numeroSocio) {
        for (Socio socio : socios) {
            if (socio.getNumeroSocio().equals(numeroSocio)) {
                return true;
            }
        }
        return false;
    }

    // Obtener todos los socios
    public static List<Socio> getSocios() {
        return socios;
    }

    // Obtener un socio específico (por número de socio)
    public static Socio getSocio(String numeroSocio) {
        for (Socio socio : socios) {
            if (socio.getNumeroSocio().equals(numeroSocio)) {
                return socio;
            }
        }
        return null;  // O puedes lanzar una excepción personalizada
    }

    // Método para eliminar un socio (por número de socio)
    public static boolean eliminarSocio(String numeroSocio) {
        for (Socio socio : socios) {
            if (socio.getNumeroSocio().equals(numeroSocio)) {
                socios.remove(socio);  // Elimina el socio si lo encuentra
                return true;  // Eliminación exitosa
            }
        }
        return false;  // Si no encuentra el socio, retorna false
    }

    // NUEVO MÉTODO: Listar socios por tipo
    public static List<Socio> listarSociosPorTipo(int tipoSocio) {
        List<Socio> sociosFiltrados = new ArrayList<>();

        for (Socio socio : socios) {
            if (tipoSocio == 0 && socio instanceof Estandar) {
                sociosFiltrados.add(socio);  // Tipo 0: Estandar
            } else if (tipoSocio == 1 && socio instanceof Federado) {
                sociosFiltrados.add(socio);  // Tipo 1: Federado
            } else if (tipoSocio == 2 && socio instanceof Infantil) {
                sociosFiltrados.add(socio);  // Tipo 2: Infantil
            }
        }

        return sociosFiltrados;
    }
}
