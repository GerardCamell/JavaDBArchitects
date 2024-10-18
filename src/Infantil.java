// Clase Infantil: Representa a un socio infantil, que tiene un número de socio del padre o madre
public class Infantil extends Socio {

    // Atributo específico del socio infantil
    private String numSocioPadreOMadre;

    // Constructor: Inicializa los atributos de un socio infantil
    public Infantil(String numeroSocio, String nombre, String numSocioPadreOMadre) {
        super(numeroSocio, nombre);
        this.numSocioPadreOMadre = numSocioPadreOMadre;
    }
    // Implementación del método abstracto para calcular la cuota mensual con descuento infantil
    @Override
    public double calcularCuotaMensual() {
        // Implementar lógica de cálculo con descuento infantil
        return 0;
    }
    // Getters y Setters para numSocioPadreOMadre
    public String getNumSocioPadreOMadre() {
        return numSocioPadreOMadre;
    }

    public void setNumSocioPadreOMadre(String numSocioPadreOMadre) {
        this.numSocioPadreOMadre = numSocioPadreOMadre;
    }

    @Override
    public String toString() {
        return "Socio Infantil:\n" +
                "\tNúmero de Socio: " + getNumeroSocio() + "\n" +
                "\tNombre: " + getNombre() + "\n" +
                "\tNúmero de Socio del Padre/Madre: " + numSocioPadreOMadre + "\n";
    }
}

