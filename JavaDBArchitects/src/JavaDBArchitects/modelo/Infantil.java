package JavaDBArchitects.modelo;

import java.math.BigDecimal;

public class Infantil extends Socio {

    private Integer numSocioPadreOMadre;

    public Infantil(int numeroSocio, String nombre, Integer numSocioPadreOMadre, BigDecimal cuotaMensual) {
        super(numeroSocio, nombre, "INFANTIL", null, null, null, numSocioPadreOMadre, cuotaMensual);
        this.numSocioPadreOMadre = numSocioPadreOMadre;
    }

    @Override
    public double calcularCuotaMensual() {
        return getCuotaMensual().doubleValue();
    }

    public
    Integer getNumSocioPadreOMadre() {
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
                        "NIF del tutor: '%s'\n" +
                        "Cuota Mensual: %s€\n",
                getNumeroSocio(), getNombre(), numSocioPadreOMadre, getCuotaMensual());
    }
}


