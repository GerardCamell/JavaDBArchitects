package JavaDBArchitects.modelo;

import java.math.BigDecimal;

public class Federado extends Socio {

    private String NIF;
    private Federacion federacion;

    public Federado(int numeroSocio, String nombre, String NIF, Federacion federacion, BigDecimal cuotaMensual) {
        super(numeroSocio, nombre, "FEDERADO", NIF, null, federacion != null ? federacion.getId_federacion() : null, null, cuotaMensual);
        this.NIF = NIF;
        this.federacion = federacion;
    }

    @Override
    public double calcularCuotaMensual() {
        return getCuotaMensual().doubleValue();
    }

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

    @Override
    public String toString() {
        return String.format("Federado \n" +
                        "Número de Socio: %d\n" +
                        "Nombre: '%s'\n" +
                        "NIF: '%s'\n" +
                        "%s\n" +
                        "Cuota Mensual: %s€\n",
                getNumeroSocio(), getNombre(), NIF, federacion.toString(), getCuotaMensual());
    }
}

