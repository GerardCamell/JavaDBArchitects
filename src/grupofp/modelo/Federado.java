package grupofp.modelo;

// Clase grupofp.modelo.Federado: Representa a un socio federado, asociado a una federación
public class Federado<Federacion> extends Socio {
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

    // Getters y Setters para NIF y grupofp.modelo.Federacion
    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public <Federacion> Federacion getFederacion() {
        return (Federacion) federacion;
    }

    public void setFederacion(Federacion federacion) {
        this.federacion = federacion;
    }

    @Override
    public String toString() {
        return "grupofp.modelo.Socio grupofp.modelo.Federado:\n" +
                "\tNúmero de grupofp.modelo.Socio: " + getNumeroSocio() + "\n" +
                "\tNombre: " + getNombre() + "\n" +
                "\tNIF: " + NIF + "\n" +
                "\tFederación: " + federacion + "\n";
    }
}
