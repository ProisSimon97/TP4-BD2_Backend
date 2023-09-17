package ar.unrn.tp.dto;

import ar.unrn.tp.modelo.Producto;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductoDto {

    private Long id;
    private String codigo;
    private String descripcion;
    private String categoria;
    private double precio;
    private String marca;

    public static ProductoDto fromDomain(Producto producto) {
        return ProductoDto.builder()
                .id(producto.id())
                .codigo(producto.codigo())
                .descripcion(producto.descripcion())
                .categoria(producto.categoria().tipo())
                .precio(producto.getPrecio())
                .marca(producto.getMarca().getTipo())
                .build();
    }
}
