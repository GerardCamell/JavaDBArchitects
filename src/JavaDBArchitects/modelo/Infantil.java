package JavaDBArchitects.modelo;

// Clase infantil: Representa a un socio infantil, que tiene un número de socio del padre o madre
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

        return 0;
    }
    // Getters y Setters para numSocioPadreOMadre
    public String getNumSocioPadreOMadre() {
        return numSocioPadreOMadre;
    }

    public void setNumSocioPadreOMadre(String numSocioPadreOMadre) {
        this.numSocioPadreOMadre = numSocioPadreOMadre;
    }
    // Método toString para proporcionar una representación en texto de la clase
    @Override
    public String toString() {
        return String.format("Infantil {\n" +
                        "    Número de Socio: '%s',\n" +
                        "    Nombre: '%s',\n" +
                        "    NIF del tutor: '%s'\n" +
                        "}",
                getNumeroSocio(),
                getNombre(),
                numSocioPadreOMadre);
    }
}
