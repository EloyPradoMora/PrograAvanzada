package usuario;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "productos")
public class Producto {
    @Id
    private String idProducto;
    private String nombre;
    private int precio;
    private String descripcion;
    private int cantidad;
    private String tipo;
    private String estado;
    private boolean esVisible;
    private String fechaPublicacion;
    private String imagenUrl;
    private String publisherUsername;

    public Producto() {
    }

    public Producto(String nombre, String descripcion, int cantidad, String tipo, String estado, boolean esVisible,
            String fechaPublicacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.estado = estado;
        this.esVisible = esVisible;
        this.fechaPublicacion = fechaPublicacion;
    }

    public Producto(String idProducto, String nombre, int precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return idProducto;
    };

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getPublisherUsername() {
        return publisherUsername;
    }

    public void setPublisherUsername(String publisherUsername) {
        this.publisherUsername = publisherUsername;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public boolean isEsVisible() {
        return esVisible;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setEsVisible(boolean esVisible) {
        this.esVisible = esVisible;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
}
