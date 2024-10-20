package JavaDBArchitects.modelo;

// Clase Federado: Representa a un socio federado, asociado a una federación
public class Federado extends Socio {
    // Atributos específicos de un socio federado
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
        // Implementar lógica de cálculo
        return 0;
    }

    // Getters y Setters para NIF y JavaDBArchitects.modelo.Federacion
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

    // Método toString para proporcionar una representación en texto de la clase
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
