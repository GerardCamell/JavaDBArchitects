package grupofp.modelo;

import java.util.Date;

// Clase grupofp.modelo.Inscripcion: Representa una inscripción de un socio a una excursión
public class Inscripcion {

    // Atributos de la inscripción
    private String numInscripcion;
    private Socio socio;
    private Excursion excursion;
    private Date fechaInscripcion;

    // Constructor: Inicializa los atributos de la inscripción

    public Inscripcion(String numInscripcion, Socio socio, Excursion excursion, Date fechaInscripcion) {
        this.numInscripcion = numInscripcion;
        this.socio = socio; // Composición: el socio es parte de la inscripción
        this.excursion = excursion; // Composición: la excursión es parte de la inscripción
        this.fechaInscripcion = fechaInscripcion;
    }

    // Getters y Setters
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

    // Método para cancelar la inscripción
    public void cancelarInscripcion() {
        // Implementar lógica de cancelación de la inscripción
    }

    // Método toString
    @Override
    public String toString() {
        return "grupofp.modelo.Inscripcion{" +
                "numInscripcion='" + numInscripcion + '\'' +
                ", socio=" + socio +
                ", excursion=" + excursion +
                ", fechaInscripcion=" + fechaInscripcion +
                '}';
    }
}
