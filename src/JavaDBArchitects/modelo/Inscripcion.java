package JavaDBArchitects.modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * La clase Inscripción representa el registro de un socio en una excursión.
 */
public class Inscripcion {

    // Formateador para representar las fechas en formato "dd/MM/yyyy"
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Atributos de la clase Inscripción
    private String numInscripcion;  // Número de inscripción único
    private Socio socio;            // Socio asociado a la inscripción
    private Excursion excursion;    // Excursión asociada a la inscripción
    private Date fechaInscripcion;  // Fecha en la que se realizó la inscripción

    // Constructor: Inicializa los atributos de la inscripción
    public Inscripcion(String numInscripcion, Socio socio, Excursion excursion, Date fechaInscripcion) {
        this.numInscripcion = numInscripcion;
        this.socio = socio;
        this.excursion = excursion;
        this.fechaInscripcion = fechaInscripcion;
    }

    // Getters y Setters: Permiten acceder y modificar los atributos

    public String getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(String numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Excursion getExcursion() {
        return excursion;
    }

    public void setExcursion(Excursion excursion) {
        this.excursion = excursion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    /**
     * Proporciona una representación en texto de la inscripción, incluyendo el socio,
     * la excursión y la fecha de inscripción.
     *
     * @return una cadena con los detalles de la inscripción.
     */
    @Override
    public String toString() {
        LocalDate fechaInscripcionLocal = fechaInscripcion.toLocalDate();
        return "Nº Inscripción: " + numInscripcion + '\n' +
                "Tipo de Socio: " + socio +
                "-- Excursión -- \n" + excursion + '\n' +
                "Fecha de la Inscripción: " + fechaInscripcionLocal.format(FORMATO_FECHA);
    }
}
