package JavaDBArchitects.modelo;

// Clase Seguro: Representa el seguro que puede tener un socio (básico o completo)
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
    // Método toString para proporcionar una representación en texto de la clase
    @Override
    public String toString() {
        return String.format("Tipo: '%s', Precio: %.2f € ", tipo, precio);
    }
    public class Socio {
        public boolean modificarSeguro(Seguro seguro) {
            return false;  // Implementación por defecto que retorna false
        }
    }

    public class Estandar extends Socio {
        private Seguro seguro;

        @Override
        public boolean modificarSeguro(Seguro seguro) {
            this.seguro = seguro;
            return true;  // En Estandar, el seguro se puede modificar
        }
    }

}
