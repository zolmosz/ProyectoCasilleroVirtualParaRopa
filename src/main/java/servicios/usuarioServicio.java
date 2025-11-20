package servicios;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import repositorios.usuarioRepositorio;
import entidades.usuario;
import dtos.UsuarioUpdateDTO;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class usuarioServicio {

    private usuarioRepositorio usuarioRepositorio;
    private casilleroServicio casilleroServicio; // inyectado para crear casillero automáticamente

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

        return usuarioRepositorio
                .find("email = ?1 and contrasenia = ?2", correo, contrasenia)
                .firstResult();
    }

    // ============================================
    // NUEVO MÉTODO: Obtener usuario por email
    // ============================================
    public usuario getUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        return usuarioRepositorio
                .find("email", email)
                .firstResult();
    }

    @Transactional
    public usuario addUsuario(usuario usuario) {
        this.usuarioRepositorio.persist(usuario);
        try {
            // Intentar crear casillero para el usuario recién persistido
            casilleroServicio.crearCasillero(usuario);
        } catch (IllegalStateException ex) {
            // Si el casillero ya existe, ignorar para no afectar la creación del usuario
        }
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
    public usuario updateUsuario(Long id, UsuarioUpdateDTO usuarioUpdate) {
        var usuarioActualizado = usuarioRepositorio.findById(id);
        if (usuarioActualizado == null) {
            throw new RuntimeException("No se encontró el usuario con id " + id);
        }
        if (usuarioUpdate.getNombre() != null && !usuarioUpdate.getNombre().isEmpty()) {
            usuarioActualizado.setNombre(usuarioUpdate.getNombre());
        }
        if (usuarioUpdate.getApellidos() != null && !usuarioUpdate.getApellidos().isEmpty()) {
            usuarioActualizado.setApellidos(usuarioUpdate.getApellidos());
        }
        if (usuarioUpdate.getCedula() != null && !usuarioUpdate.getCedula().isEmpty()) {
            usuarioActualizado.setCedula(usuarioUpdate.getCedula());
        }
        if (usuarioUpdate.getEmail() != null && !usuarioUpdate.getEmail().isEmpty()) {
            usuarioActualizado.setEmail(usuarioUpdate.getEmail());
        }
        if (usuarioUpdate.getTelefono() != null && !usuarioUpdate.getTelefono().isEmpty()) {
            usuarioActualizado.setTelefono(usuarioUpdate.getTelefono());
        }
        if (usuarioUpdate.getDireccionEntrega() != null && !usuarioUpdate.getDireccionEntrega().isEmpty()) {
            usuarioActualizado.setDireccionEntrega(usuarioUpdate.getDireccionEntrega());
        }
        if (usuarioUpdate.getImagen() != null && !usuarioUpdate.getImagen().isEmpty()) {
            usuarioActualizado.setImagen(usuarioUpdate.getImagen());
        }
        if (usuarioUpdate.getFechaNacimiento() != null) {
            usuarioActualizado.setFechaNacimiento(usuarioUpdate.getFechaNacimiento());
        }
        return usuarioActualizado;
    }

    @Transactional
    public void deleteUsuario(long id) {
        this.usuarioRepositorio.deleteById(id);
    }
}
