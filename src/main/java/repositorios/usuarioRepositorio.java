package repositorios;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import entidades.usuario;

@ApplicationScoped
public class usuarioRepositorio implements PanacheRepository<usuario> {
}
