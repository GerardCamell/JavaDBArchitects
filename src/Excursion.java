import java.util.Date;

// Clase que representa una excursión organizada por el centro
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

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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

    // Método toString: Nos proporciona una representación en texto de la clase
    @Override
    public String toString() {
        return "Excursion{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha=" + fecha +
                ", numDias=" + numDias +
                ", precioInscripcion=" + precioInscripcion +
                '}';
    }
}
