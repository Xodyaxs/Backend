package libreria.ms_pedidos.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String clienteEmail;

    @NotBlank
    @Column(length = 1000)
    private String detalleProductos;

    @Min(0)
    private Integer total;

    private String estado;

    private LocalDateTime fecha;

    public Pedido() {
        this.fecha = LocalDateTime.now();
        this.estado = "CONFIRMADO";
    }

    public Pedido(String clienteEmail, String detalleProductos, Integer total) {
        this.clienteEmail = clienteEmail;
        this.detalleProductos = detalleProductos;
        this.total = total;
        this.estado = "CONFIRMADO";
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }
    public String getDetalleProductos() { return detalleProductos; }
    public void setDetalleProductos(String detalleProductos) { this.detalleProductos = detalleProductos; }
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}