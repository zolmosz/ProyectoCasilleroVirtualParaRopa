package servicios;
import entidades.articulo;
import entidades.usuario;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import repositorios.usuarioRepositorio;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class usuarioServicio {
    private usuarioRepositorio usuarioRepositorio;

    public usuario getUsuario(Long id) {
        return this.usuarioRepositorio.findById(id);
    }

    @Transactional
    public usuario addUsuario(usuario usuario) {
        this.usuarioRepositorio.persist(usuario);
        return usuario;
    }

    @Transactional
    public usuario cambiarContrasenia(long id,usuario usuario) {
        this.usuarioRepositorio.persist(usuario);
        return usuario;
    }

}
