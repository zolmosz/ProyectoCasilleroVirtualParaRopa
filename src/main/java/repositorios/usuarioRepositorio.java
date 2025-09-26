package repositorios;

import entidades.usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class usuarioRepositorio implements PanacheRepository<usuario> {
}
