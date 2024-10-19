package JavaDBArchitects.modelo;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Excursion {

    // Atributos que definen los datos básicos de una excursión
    private String codigo;
    private String descripcion;
    private Date fecha;
    private int numDias;
    private float precioInscripcion;

    // Constructor: Inicializa los atributos de la clase
    public Excursion(String codigo, String descripcion, Date fecha, int numDias, float precioInscripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.numDias = numDias;
        this.precioInscripcion = precioInscripcion;
    }

    // Getters y Setters: Nos permiten acceder y modificar los atributos desde otras clases

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    // Método para obtener la fecha como LocalDate
    public LocalDate getFechaAsLocalDate() {
        if (fecha instanceof java.sql.Date) {
            // Convertir java.sql.Date a java.time.LocalDate directamente
            return ((java.sql.Date) fecha).toLocalDate();
        } else {
            // Convertir java.util.Date a LocalDate usando Instant
            return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    // También puedes agregar un método setter que acepte LocalDate
    public void setFecha(LocalDate fechaLocalDate) {
        this.fecha = Date.from(fechaLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }

    public float getPrecioInscripcion() {
        return precioInscripcion;
    }

    public void setPrecioInscripcion(float precioInscripcion) {
        this.precioInscripcion = precioInscripcion;
    }

    // Método toString modificado para mostrar la fecha formateada
    @Override
    public String toString() {
        return "Código: " + codigo + "\n" +
                "Descripción: " + descripcion + "\n" +
                "Fecha: " + getFechaAsLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                "Número de días: " + numDias + "\n" +
                "Precio inscripción: " + precioInscripcion;
    }
}
