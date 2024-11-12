package JavaDBArchitects.modelo;

public class Seguro {
    private TipoSeguro tipo;
    private float precio;

    public Seguro(TipoSeguro tipo, float precio) {
        this.tipo = tipo;
        this.precio = precio;
    }

    public TipoSeguro getTipo() {
        return tipo;
    }

    public void setTipo(TipoSeguro tipo) {
        this.tipo = tipo;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Seguro{" +
                "tipo=" + tipo +
                ", precio=" + precio +
                '}';
    }
}
