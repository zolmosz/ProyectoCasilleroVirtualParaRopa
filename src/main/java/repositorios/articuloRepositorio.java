package repositorios;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import entidades.articulo;

@ApplicationScoped
public class articuloRepositorio implements PanacheRepository<articulo> {
}
