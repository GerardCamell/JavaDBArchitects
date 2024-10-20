package JavaDBArchitects.modelo;

// Clase Federado: Representa a un socio federado, asociado a una federación
public class Federado extends Socio {

    // Atributos específicos de un socio federado: NIF y federación asociada
    private String NIF;
    private Federacion federacion;

    // Constructor: Inicializa los atributos de un socio federado
    public Federado(String numeroSocio, String nombre, String NIF, Federacion federacion) {
        super(numeroSocio, nombre);
        this.NIF = NIF;
        this.federacion = federacion; //Composición: El socio federado tiene que pertenecer a una Federación
    }

    // Implementación del método abstracto para calcular la cuota mensual
    @Override
    public double calcularCuotaMensual() {
        // Implementar lógica de cálculo para un socio federado
        return 0; // Implementar el cálculo adecuado para la cuota mensual
    }

    // Getters y Setters: Métodos para acceder y modificar los atributos NIF y federación

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public Federacion getFederacion() {
        return federacion;
    }

    public void setFederacion(Federacion federacion) {
        this.federacion = federacion;
    }

    // Método toString: Proporciona una representación en texto de la clase Federado
    @Override
    public String toString() {
        return String.format("Federado \n" +
                        "Número de Socio: '%s',\n" +
                        "Nombre: '%s',\n" +
                        "NIF: '%s',\n" +
                        "%s\n",
                getNumeroSocio(), getNombre(), NIF, federacion.toString());
    }
}

