package grupofp.modelo;

// Clase grupofp.modelo.Estandar: Representa a un socio estándar con un seguro
public class Estandar extends Socio {

    // Atributos específicos de un socio estándar
    private String NIF;
    private Seguro seguro;

    // Constructor: Inicializa los atributos de un socio de tipo estándar
    public Estandar(String numeroSocio, String nombre, String NIF, Seguro seguro) {
        super(numeroSocio, nombre);
        this.NIF = NIF;
        this.seguro = seguro; //Composición: El seguro es obligatorio para el socio estándar
    }

    // Implementación del método abstracto para calcular la cuota mensual
    @Override
    public double calcularCuotaMensual() {
        // Aquí se puede implementar la lógica de cálculo de la cuota para un socio estándar
        return 0;
    }

    // Getters y Setters para NIF y grupofp.modelo.Seguro
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

    @Override
    public String toString() {
        return "grupofp.modelo.Socio Estándar:\n" +
                "\tNúmero de grupofp.modelo.Socio: " + getNumeroSocio() + "\n" +
                "\tNombre: " + getNombre() + "\n" +
                "\tNIF: " + NIF + "\n" +
                "\tgrupofp.modelo.Seguro: " + seguro.toString() + "\n";
    }
}
