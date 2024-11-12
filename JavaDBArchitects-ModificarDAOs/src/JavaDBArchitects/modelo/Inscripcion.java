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
    private String id_inscripcion;  // Número de inscripción único
    private Socio socio;            // Socio asociado a la inscripción
    private Excursion excursion;    // Excursión asociada a la inscripción
    private Date fecha_inscripcion;  // Fecha en la que se realizó la inscripción

    // Constructor: Inicializa los atributos de la inscripción
    public Inscripcion(String numInscripcion, Socio socio, Excursion excursion, Date fecha_inscripcion) {
        this.id_inscripcion = numInscripcion;
        this.socio = socio;
        this.excursion = excursion;
        this.fecha_inscripcion = fecha_inscripcion;
    }


    // Getters y Setters: Permiten acceder y modificar los atributos

    public String getNumInscripcion() {
        return id_inscripcion;
    }

    public void setNumInscripcion(String numInscripcion) {
        this.id_inscripcion = numInscripcion;
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

    public Date getFecha_inscripcion() {
        return fecha_inscripcion;
    }

    public void setFecha_inscripcion(Date fecha_inscripcion) {
        this.fecha_inscripcion = fecha_inscripcion;
    }

    /**
     * Proporciona una representación en texto de la inscripción, incluyendo el socio,
     * la excursión y la fecha de inscripción.
     *
     * @return una cadena con los detalles de la inscripción.
     */
    @Override
    public String toString() {
        LocalDate fechaInscripcionLocal = fecha_inscripcion.toLocalDate();
        return "Nº Inscripción: " + id_inscripcion + '\n' +
                "Tipo de Socio: " + socio +
                "-- Excursión -- \n" + excursion + '\n' +
                "Fecha de la Inscripción: " + fechaInscripcionLocal.format(FORMATO_FECHA);
    }
}
