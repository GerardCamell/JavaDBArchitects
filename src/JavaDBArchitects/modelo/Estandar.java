package JavaDBArchitects.modelo;

// Clase JavaDBArchitects.modelo.Estandar: Representa a un socio estándar con un seguro
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

    // Getters y Setters para NIF y JavaDBArchitects.modelo.Seguro
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

    //He modificado el tostring para que aparezcan los datos completos

    // Método toString para proporcionar una representación en texto de la clase
    @Override
    public String toString() {
        return "Estandar \n" +
                "    Número de Socio: '" + getNumeroSocio() + "'\n" +
                "    Nombre: '" + getNombre() + "'\n" +
                "    NIF: '" + getNIF() + "'\n" +
                "    Seguro: " + seguro + "\n";
    }
    @Override
    public boolean modificarSeguro(Seguro nuevoSeguro) {
        this.seguro = nuevoSeguro;  // Modifica el seguro
        return true;  // Indica que la modificación fue exitosa
    }

}