package grupofp.modelo;

// Clase grupofp.modelo.Seguro: Representa el seguro que puede tener un socio (básico o completo)
public class Seguro {

    // Atributos del seguro
    private String tipo;  // Enum: Básico o Completo
    private float precio;

    // Constructor: Inicializa los atributos del seguro
    public Seguro(String tipo, float precio) {
        this.tipo = tipo;
        this.precio = precio;
    }

    // Getters y Setters para tipo y precio
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "grupofp.modelo.Seguro:\n" +
                "\t\tTipo: " + tipo + "\n" +
                "\t\tPrecio: " + precio;
    }
}
