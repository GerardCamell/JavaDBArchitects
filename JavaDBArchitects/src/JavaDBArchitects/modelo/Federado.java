package JavaDBArchitects.modelo;

public class Federado extends Socio {

    private String NIF;
    private Federacion federacion;

    public Federado(int numeroSocio, String nombre, String NIF, Federacion federacion) {
        super(numeroSocio, nombre, "FEDERADO", NIF, null, federacion != null ? federacion.getId_federacion() : null, null);
        this.NIF = NIF;
        this.federacion = federacion;
    }

    @Override
    public double calcularCuotaMensual() {
        return 0;
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
                        "%s\n",
                getNumeroSocio(), getNombre(), NIF, federacion.toString());
    }
}
