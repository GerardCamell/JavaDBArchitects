package grupofp;

import grupofp.modelo.Inscripcion;

import java.util.HashSet;
import java.util.Set;

// Clase grupofp.GestorInscripciones
public class GestorInscripciones {

    // Conjunto para almacenar inscripciones únicas
    private Set<Inscripcion> inscripciones;

    // Constructor: Inicializa el conjunto de inscripciones
    public GestorInscripciones() {
        inscripciones = new HashSet<>();
    }

    // Método para agregar una inscripción
    public boolean agregarInscripcion(Inscripcion inscripcion) {
        return inscripciones.add(inscripcion); // Retorna true si se agrega exitosamente
    }

    // Método para listar todas las inscripciones
    public void listarInscripciones() {
        System.out.println("Lista de Inscripciones:");
        for (Inscripcion inscripcion : inscripciones) {
            System.out.println(inscripcion);
        }
    }

    // Método para cancelar una inscripción
    public boolean cancelarInscripcion(Inscripcion inscripcion) {
        return inscripciones.remove(inscripcion); // Retorna true si se elimina exitosamente
    }
}