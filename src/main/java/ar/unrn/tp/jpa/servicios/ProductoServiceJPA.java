package ar.unrn.tp.jpa.servicios;

import ar.unrn.tp.api.ProductoService;
import ar.unrn.tp.modelo.Categoria;
import ar.unrn.tp.modelo.Marca;
import ar.unrn.tp.modelo.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ProductoServiceJPA implements ProductoService {

    private EntityManagerFactory emf;

    public ProductoServiceJPA(EntityManagerFactory emf) {
        this.emf = emf;
    }
    @Override
    public void crearProducto(String codigo, String descripcion, double precio, String marca, Long idCategoria) {
        inTransactionExecute((em) -> {
            Categoria categoria = em.find(Categoria.class, idCategoria);

            if(categoria == null) {
                throw new RuntimeException("La categoria solicitada no existe");
            }

            Marca marcaProducto = new Marca(marca);
            Producto producto = new Producto(codigo, descripcion, categoria, precio, marcaProducto);
            em.persist(producto);
        });
    }

    @Override
    public void modificarProducto(Long idProducto, String codigo, String descripcion, double precio, Long idCategoria) {
        inTransactionExecute((em) -> {
            Categoria categoria = em.find(Categoria.class, idCategoria);
            Producto producto = em.find(Producto.class, idProducto);

            if(producto == null) {
                throw new RuntimeException("El producto solicitado no existe");
            }

            if(categoria == null) {
                throw new RuntimeException("La categoria solicitada no existe");
            }

            producto.codigo(codigo);
            producto.descripcion(descripcion);
            producto.precio(precio);
            producto.categoria(categoria);
        });
    }

    @Override
    public List listarProductos() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            TypedQuery<Producto> q = em.createQuery("select p from Producto p", Producto.class);
            return q.getResultList();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void inTransactionExecute(Consumer<EntityManager> bloqueDeCodigo) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            bloqueDeCodigo.accept(em);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }
}
