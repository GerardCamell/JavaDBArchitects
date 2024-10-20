package JavaDBArchitects.modelo;

import java.util.ArrayList;
import java.util.List;

public class ListaSocios {

    // Lista que almacena todos los socios registrados
    private static List<Socio> socios = new ArrayList<>();

    // Método que carga datos iniciales de socios, si es necesario
    public static void cargarDatosSocios() {
        // Si necesitas cargar datos predeterminados, lo puedes hacer aquí
    }

    // Agrega un socio a la lista de socios
    public static void addSocio(Socio socio) {
        socios.add(socio);
    }

    // Comprueba si un socio ya existe en la lista (por número de socio)
    public static boolean socioExiste(String numeroSocio) {
        for (Socio socio : socios) {
            if (socio.getNumeroSocio().equals(numeroSocio)) {
                return true; // El socio ya existe
            }
        }
        return false; // El socio no existe
    }

    // Retorna la lista completa de socios
    public static List<Socio> getSocios() {
        return socios;
    }

    // Obtiene un socio específico usando su número de socio
    public static Socio getSocio(String numeroSocio) {
        for (Socio socio : socios) {
            if (socio.getNumeroSocio().equals(numeroSocio)) {
                return socio; // Devuelve el socio si lo encuentra
            }
        }
        return null;  // Si no lo encuentra, retorna null
    }

    // Elimina un socio de la lista basado en su número de socio
    public static boolean eliminarSocio(String numeroSocio) {
        for (Socio socio : socios) {
            if (socio.getNumeroSocio().equals(numeroSocio)) {
                socios.remove(socio);  // Elimina el socio si lo encuentra
                return true;  // Eliminación exitosa
            }
        }
        return false;  // Retorna false si el socio no existe
    }

    // Lista a los socios según su tipo (Estandar, Federado o Infantil)
    public static List<Socio> listarSociosPorTipo(int tipoSocio) {
        List<Socio> sociosFiltrados = new ArrayList<>();

        // Filtra los socios por su tipo
        for (Socio socio : socios) {
            if (tipoSocio == 0 && socio instanceof Estandar) {
                sociosFiltrados.add(socio);  // Tipo 0: Socio Estandar
            } else if (tipoSocio == 1 && socio instanceof Federado) {
                sociosFiltrados.add(socio);  // Tipo 1: Socio Federado
            } else if (tipoSocio == 2 && socio instanceof Infantil) {
                sociosFiltrados.add(socio);  // Tipo 2: Socio Infantil
            }
        }

        return sociosFiltrados; // Retorna la lista filtrada por tipo
    }
}
