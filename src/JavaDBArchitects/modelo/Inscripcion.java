package JavaDBArchitects.modelo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Inscripcion {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private String numInscripcion;
    private Socio socio;
    private Excursion excursion;
    private Date fechaInscripcion;

    public Inscripcion(String numInscripcion, Socio socio, Excursion excursion, Date fechaInscripcion) {
        this.numInscripcion = numInscripcion;
        this.socio = socio;
        this.excursion = excursion;
        this.fechaInscripcion = fechaInscripcion;
    }

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

    public Date getFechaInscripcion() {return fechaInscripcion;}

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    @Override
    public String toString() {
        LocalDate fechaInscripcionLocal = fechaInscripcion.toLocalDate();
        return "Nº Inscripcion: " + numInscripcion + '\n' +"Tipo de Socio: " + socio + "-- Excursion -- \n" + excursion + '\n' +
                "Fecha de la Inscripción: " + fechaInscripcionLocal.format(FORMATO_FECHA);
    }
}
