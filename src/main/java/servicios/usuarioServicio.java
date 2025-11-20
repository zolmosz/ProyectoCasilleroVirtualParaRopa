package servicios;

import entidades.articulo;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
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
    private Mailer mailer;

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
        return usuarioActualizado;
    }

    @Transactional
    public void deleteUsuario(long id) {
        this.usuarioRepositorio.deleteById(id);
    }

    /**
     * Envía la contraseña del usuario por correo electrónico.
     * ADVERTENCIA DE SEGURIDAD: Este método envía contraseñas en texto plano,
     * lo cual NO es una práctica recomendada. Se recomienda implementar un flujo
     * de restablecimiento de contraseña con token temporal en lugar de enviar
     * la contraseña actual.
     * 
     * @param email El correo electrónico del usuario
     * @throws IllegalArgumentException si el email es nulo o vacío
     * @throws RuntimeException si no se encuentra un usuario con el correo proporcionado
     */
    public void enviarContraseniaPorCorreo(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        
        usuario u = usuarioRepositorio.find("email", email).firstResult();
        if (u == null) {
            throw new RuntimeException("No se encontró un usuario con el correo proporcionado");
        }
        
        String contrasenia = u.getContrasenia();
        String nombre = (u.getNombre() != null && !u.getNombre().isEmpty()) ? u.getNombre() : "usuario";
        
        String cuerpo = "Hola " + nombre + ",\n\n"
                + "Tu contraseña actual es: " + contrasenia + "\n\n"
                + "Si no solicitaste esto, por favor contacta al soporte.\n\n"
                + "Saludos,\nEquipo de Casillero Virtual";
        
        mailer.send(Mail.withText(email, "Recuperación de contraseña - Casillero Virtual", cuerpo));
    }
}
