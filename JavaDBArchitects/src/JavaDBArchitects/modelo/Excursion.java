package JavaDBArchitects.modelo;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una excursión en el sistema.
 * Cada excursión tiene un código, una descripción, una fecha de inicio, un número de días
 * y un precio de inscripción.
 */
 public class Excursion {
    private String idExcursion; // Cambiado de "codigo" a "idExcursion"
    private String descripcion;
    private Date fecha;
    private int numDias;
    private float precioInscripcion;

    // Constructor actualizado
    public Excursion(String idExcursion, String descripcion, Date fecha, int numDias, float precioInscripcion) {
        this.idExcursion = idExcursion;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.numDias = numDias;
        this.precioInscripcion = precioInscripcion;
    }


    // Getters y setters estándar para acceder y modificar los atributos de la clase.
    // Permiten obtener o establecer el código, descripción, fecha, número de días y precio de inscripción.

    public String getIdExcursion() {
        return idExcursion;
    }

    public void setIdExcursion(String idExcursion) {
        this.idExcursion = idExcursion;
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

    /**
     * Método para obtener la fecha de la excursión en formato LocalDate.
     *
     * @return La fecha de la excursión como LocalDate.
     */
    public LocalDate getFechaAsLocalDate() {
        if (fecha instanceof java.sql.Date) {
            // Convertir java.sql.Date a java.time.LocalDate directamente
            return ((java.sql.Date) fecha).toLocalDate();
        } else {
            // Convertir java.util.Date a LocalDate usando Instant
            return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    /**
     * Calcula la fecha de finalización de la excursión en base al número de días.
     *
     * @return La fecha de finalización de la excursión.
     */
    public LocalDate calcularFechaFin() {
        return getFechaAsLocalDate().plusDays(numDias - 1); // Calcular días de la excursión
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Método para establecer la fecha usando un LocalDate.
     *
     * @param fechaLocalDate La fecha a establecer como LocalDate.
     */
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

    /**
     * Proporciona una representación en texto de la excursión,
     * mostrando el código, descripción, fecha, número de días y el precio de inscripción.
     *
     * @return Representación en texto de los datos de la excursión.
     */
    @Override
    public String toString() {
        String newPrecio; // Cambiar formato del precio en función si es entero o float
        if (precioInscripcion % 1 == 0) {
            newPrecio = String.format("%d", (int) precioInscripcion); // Si es entero
        } else {
            newPrecio = String.format("%.2f", precioInscripcion); // Si es float
        }

        return "Código: " + idExcursion + "\n" +
                "Descripción: " + descripcion + "\n" +
                "Fecha: " + getFechaAsLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                "Número de días: " + numDias + "\n" +
                "Precio inscripción: " + newPrecio + " €";
    }
}

