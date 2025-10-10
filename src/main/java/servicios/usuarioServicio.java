package servicios;

import entidades.articulo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import repositorios.usuarioRepositorio;
import entidades.usuario;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class usuarioServicio {
    private usuarioRepositorio usuarioRepositorio;

    public usuario getUsuario(Long id) {
        return this.usuarioRepositorio.findById(id);
    }

    public List<usuario> findAll() {
        return this.usuarioRepositorio.listAll();
    }
    public usuario getUsuarioRegistrado(String correo, String contrasenia) {
        if (correo == null || contrasenia == null) {
            throw new IllegalArgumentException("El correo y la contraseña son obligatorios");
        }

        return usuarioRepositorio.find("email = ?1 and contrasenia = ?2", correo, contrasenia).firstResult();
    }

    @Transactional
    public usuario addUsuario(usuario usuario) {
        this.usuarioRepositorio.persist(usuario);
        return usuario;
    }

    @Transactional
    public usuario cambiarContrasenia(Long id, usuario usuario) {
        var usuarioActualizado = usuarioRepositorio.findById(id);
        if (usuarioActualizado == null) {
            throw new RuntimeException("No se encontró el usuario con id " + id);
        }
        usuarioActualizado.setContrasenia(usuario.getContrasenia());
        return usuarioActualizado;
    }

    public String getContrasenia(Long id) {
        return this.usuarioRepositorio.findById(id).getContrasenia();
    }

    @Transactional
    public void deleteUsuario(long id) {
        this.usuarioRepositorio.deleteById(id);
    }
}
