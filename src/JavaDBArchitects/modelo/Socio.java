package JavaDBArchitects.modelo;

// Clase abstracta Socio: Representa a un socio del centro
public abstract class Socio {

    // Atributos comunes para todos los tipos de socios
    private String numeroSocio;
    private String nombre;

    // Constructor: Inicializa los atributos de un socio
    public Socio(String numeroSocio, String nombre) {
        this.numeroSocio = numeroSocio; //Composición: el socio es parte de la inscripción
        this.nombre = nombre; //Composición: la excursión es parte de la inscripción
    }

    // Getters y Setters: Para acceder y modificar los atributos
    public String getNumeroSocio() {
        return numeroSocio;
    }

    public void setNumeroSocio(String numeroSocio) {
        this.numeroSocio = numeroSocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Método abstracto: Cada tipo de socio implementará su propia lógica para calcular la cuota mensual
    public abstract double calcularCuotaMensual();

    // Método toString: Proporciona una representación en texto de la clase
    @Override
    public String toString() {
        return "numeroSocio='" + numeroSocio + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
