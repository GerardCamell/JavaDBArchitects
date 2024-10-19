package JavaDBArchitects.modelo;

// Clase Federacion: Representa una federación a la que un socio federado puede estar asociado
public class Federacion {
    // Atributos de la federación
    private String federacion;
    private String nombre;

    // Constructor: Inicializa los atributos de la federación
    public Federacion(String federacion, String nombre) {
        this.federacion = federacion;
        this.nombre = nombre;
    }
    // Getters y Setters para federacion y nombre
    public String getFederacion() {
        return federacion;
    }

    public void setFederacion(String federacion) {
        this.federacion = federacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método toString para proporcionar una representación en texto de la clase
    @Override
    public String toString() {
        return  "Codigo y nombre" +  federacion + '\'' + nombre + '\'';
    }
}
