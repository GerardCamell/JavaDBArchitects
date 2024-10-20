package JavaDBArchitects.modelo;

// Clase Infantil: Representa a un socio infantil, asociado al número de socio de su padre o madre.
public class Infantil extends Socio {

    // Número de socio del padre o madre (tutor)
    private String numSocioPadreOMadre;

    // Constructor que inicializa los atributos del socio infantil.
    public Infantil(String numeroSocio, String nombre, String numSocioPadreOMadre) {
        super(numeroSocio, nombre);
        this.numSocioPadreOMadre = numSocioPadreOMadre;
    }

    // Implementación del cálculo de la cuota mensual específica para un socio infantil. En este caso, retorna 0.
    @Override
    public double calcularCuotaMensual() {
        return 0;  // Se puede modificar si en el futuro se requiere calcular cuotas para socios infantiles.
    }

    // Métodos getter y setter para acceder y modificar el número de socio del padre o madre.
    public String getNumSocioPadreOMadre() {
        return numSocioPadreOMadre;
    }

    public void setNumSocioPadreOMadre(String numSocioPadreOMadre) {
        this.numSocioPadreOMadre = numSocioPadreOMadre;
    }

    // Método toString que devuelve una representación en texto del socio infantil.
    @Override
    public String toString() {
        return String.format("Infantil {\n" +
                "Número de Socio: '%s',\n" +
                "Nombre: '%s',\n" +
                "NIF del tutor: '%s'\n" +
                "}", getNumeroSocio(), getNombre(), numSocioPadreOMadre);
    }
}
