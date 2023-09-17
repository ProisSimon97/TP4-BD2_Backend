package ar.unrn.tp.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate fecha;
    @ManyToOne
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_venta")
    private List<ProductoVendido> productosComprados;
    @Column(name = "monto_total")
    private double montoTotal;

    protected Venta() { }

    public Venta(LocalDate fechaHora, Cliente cliente, List<ProductoVendido> productosComprados, double montoTotal) {
        this.fecha = fechaHora;
        this.cliente = cliente;
        this.productosComprados = productosComprados;
        this.montoTotal = montoTotal;
    }
}