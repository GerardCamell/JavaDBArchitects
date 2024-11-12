package JavaDBArchitects.modelo;

import java.math.BigDecimal;

public class Estandar extends Socio {

    private String NIF;
    private Seguro seguro;

    public Estandar(int numeroSocio, String nombre, String NIF, Seguro seguro, BigDecimal cuotaMensual) {
        super(numeroSocio, nombre, "ESTANDAR", NIF, seguro != null ? seguro.getTipo() : null, null, null, cuotaMensual);
        this.NIF = NIF;
        this.seguro = seguro;
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

    public TipoSeguro getTipoSeguro() {
        return seguro != null ? seguro.getTipo() : null;
    }

    public Seguro getSeguro() {
        return seguro;
    }

    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
    }

    @Override
    public String toString() {
        return "Estandar \n" +
                "Número de Socio: " + getNumeroSocio() + "\n" +
                "Nombre: '" + getNombre() + "'\n" +
                "NIF: '" + getNIF() + "'\n" +
                "Seguro: " + seguro + "\n" +
                "Cuota Mensual: " + getCuotaMensual() + "€\n";
    }
}
