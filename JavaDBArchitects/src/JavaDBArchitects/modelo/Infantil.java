package JavaDBArchitects.modelo;

public class Infantil extends Socio {

    private Integer numSocioPadreOMadre;

    public Infantil(int numeroSocio, String nombre, Integer numSocioPadreOMadre) {
        super(numeroSocio, nombre, "INFANTIL", null, null, null, numSocioPadreOMadre);
        this.numSocioPadreOMadre = numSocioPadreOMadre;
    }

    @Override
    public double calcularCuotaMensual() {
        return 0;
    }

    public Integer getNumSocioPadreOMadre() {
        return numSocioPadreOMadre;
    }

    public void setNumSocioPadreOMadre(Integer numSocioPadreOMadre) {
        this.numSocioPadreOMadre = numSocioPadreOMadre;
    }

    @Override
    public String toString() {
        return String.format("Infantil \n" +
                        "Número de Socio: %d\n" +
                        "Nombre: '%s'\n" +
                        "NIF del tutor: '%s'\n",
                getNumeroSocio(), getNombre(), numSocioPadreOMadre);
    }
}
