package JavaDBArchitects.modelo;

/**
 * Clase que representa a un socio estándar dentro del sistema.
 * Los socios de tipo estándar tienen un NIF y un seguro obligatorio.
 */
public class Estandar extends Socio {

    // Atributos específicos de un socio estándar
    private String NIF;
    private Seguro seguro; // Composición: el seguro es obligatorio para el socio estándar

    /**
     * Constructor que inicializa los atributos de un socio estándar.
     *
     * @param numeroSocio Número único del socio.
     * @param nombre Nombre del socio.
     * @param NIF NIF del socio.
     * @param seguro Seguro asociado al socio.
     */
    public Estandar(String numeroSocio, String nombre, String NIF, Seguro seguro) {
        super(numeroSocio, nombre);
        this.NIF = NIF;
        this.seguro = seguro;
    }

    /**
     * Método que calcula la cuota mensual de un socio estándar.
     * Implementación actual no calcula la cuota.
     *
     * @return Cuota mensual del socio (0 en esta implementación).
     */
    @Override
    public double calcularCuotaMensual() {
        // Implementar lógica para calcular la cuota de un socio estándar si es necesario
        return 0;
    }

    // Getters y setters estándar para los atributos del socio.
    // Los getters obtienen los valores de los atributos NIF y Seguro,
    // y los setters permiten modificar dichos valores.

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public Seguro getSeguro() {
        return seguro;
    }

    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
    }

    /**
     * Método que proporciona una representación en texto del socio estándar.
     *
     * @return Representación en texto de los datos del socio estándar.
     */
    @Override
    public String toString() {
        return "Estandar \n" +
                "Número de Socio: '" + getNumeroSocio() + "'\n" +
                "Nombre: '" + getNombre() + "'\n" +
                "NIF: '" + getNIF() + "'\n" +
                "Seguro: " + seguro + "\n";
    }
}
