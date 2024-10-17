package grupofp.controlador;

import java.util.ArrayList;
import java.util.List;

// Clase genérica que manejará cualquier tipo de objeto
public class Gestor<T> {

    // Lista de objetos del tipo genérico T
    private List<T> lista;

    // Constructor que inicializa la lista
    public Gestor() {
        lista = new ArrayList<>();
    }

    // Método para añadir un objeto a la lista
    public void añadir(T objeto) {
        lista.add(objeto);
    }

    // Método para eliminar un objeto de la lista
    public void eliminar(T objeto) {
        lista.remove(objeto);
    }

    // Método para listar todos los objetos
    public void listar() {
        for (T objeto : lista) {
            System.out.println(objeto);
        }
    }

    // Método para obtener el tamaño de la lista
    public int tamaño() {
        return lista.size();
    }
}