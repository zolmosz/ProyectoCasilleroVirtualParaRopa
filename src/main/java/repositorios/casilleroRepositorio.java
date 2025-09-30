package repositorios;

import entidades.casillero;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class casilleroRepositorio implements PanacheRepository<casillero> {
}
