package JavaDBArchitects.modelo;
import java.math.BigDecimal;


/**
 * Clase abstracta Socio: Representa a un socio del centro.
 * Contiene atributos comunes para todos los tipos de socios.
 */
public abstract class Socio {

    private int numeroSocio;       // Número único del socio
    private String nombre;         // Nombre del socio
    private String tipoSocio;      // Tipo de socio (ESTANDAR, FEDERADO, INFANTIL)
    private String nif;            // NIF del socio (opcional, depende del tipo de socio)
    private TipoSeguro tipoSeguro; // Tipo de seguro para socios ESTANDAR (BASICO o COMPLETO)
    private Integer idFederacion;  // ID de la federación asociada (opcional, depende del tipo de socio)
    private Integer idSocioPadre;  // ID del socio padre (opcional, solo para tipo infantil)
    private BigDecimal cuotaMensual;  // Nuevo atributo para cuota mensual
    /**
     * Constructor de Socio.
     *
     * @param numeroSocio    Número único del socio.
     * @param nombre         Nombre del socio.
     * @param tipoSocio      Tipo de socio (ESTANDAR, FEDERADO, INFANTIL).
     * @param nif            NIF del socio, opcional.
     * @param tipoSeguro     Tipo de seguro, opcional, para socio estándar.
     * @param idFederacion   ID de la federación, opcional.
     * @param idSocioPadre   ID del socio padre, opcional.
     */
    public Socio(int numeroSocio, String nombre, String tipoSocio, String nif, TipoSeguro tipoSeguro, Integer idFederacion, Integer idSocioPadre, BigDecimal cuotaMensual) {
        this.numeroSocio = numeroSocio;
        this.nombre = nombre;
        this.tipoSocio = tipoSocio;
        this.nif = nif;
        this.tipoSeguro = tipoSeguro;
        this.idFederacion = idFederacion;
        this.idSocioPadre = idSocioPadre;
        this.cuotaMensual = cuotaMensual;
    }

    // Getters y Setters

    public int getNumeroSocio() {
        return numeroSocio;
    }

    public void setNumeroSocio(int numeroSocio) {
        this.numeroSocio = numeroSocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoSocio() {
        return tipoSocio;
    }

    public void setTipoSocio(String tipoSocio) {
        this.tipoSocio = tipoSocio;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public TipoSeguro getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(TipoSeguro tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public Integer getIdFederacion() {
        return idFederacion;
    }

    public void setIdFederacion(Integer idFederacion) {
        this.idFederacion = idFederacion;
    }

    public Integer getIdSocioPadre() {
        return idSocioPadre;
    }

    public void setIdSocioPadre(Integer idSocioPadre) {
        this.idSocioPadre = idSocioPadre;
    }

    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    /**
     * Método abstracto que debe ser implementado por cada subclase de Socio para calcular la cuota mensual.
     *
     * @return Cuota mensual del socio.
     */
    public abstract double calcularCuotaMensual();

    /**
     * Método toString: Proporciona una representación en texto de la clase.
     *
     * @return Representación en texto de los datos del socio.
     */
    @Override
    public String toString() {
        return "Socio{" +
                "numeroSocio=" + numeroSocio +
                ", nombre='" + nombre + '\'' +
                ", tipoSocio='" + tipoSocio + '\'' +
                ", nif='" + nif + '\'' +
                ", tipoSeguro=" + tipoSeguro +
                ", idFederacion=" + idFederacion +
                ", idSocioPadre=" + idSocioPadre +
                ", cuotaMensual=" + cuotaMensual +
                '}';
    }
}


