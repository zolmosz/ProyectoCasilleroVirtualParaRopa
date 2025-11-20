package recursos;

import entidades.usuario;
import dtos.LoginDTO;
import dtos.UsuarioUpdateDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import servicios.usuarioServicio;
import servicios.EmailServicio;

import java.util.List;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class usuarioRecurso {

    @Inject
    usuarioServicio usuarioServicio;

    @Inject
    EmailServicio emailServicio;

    // =====================================
    // Crear usuario
    // =====================================
    @POST
    @Path("/add")
    public usuario addUsuario(usuario usuario) {
        return usuarioServicio.addUsuario(usuario);
    }

    // =====================================
    // Obtener todos los usuarios
    // =====================================
    @GET
    @Path("/get")
    public List<usuario> getUsuarios() {
        return usuarioServicio.findAll();
    }

    // =====================================
    // Obtener usuario por ID
    // =====================================
    @GET
    @Path("/get/{id}")
    public usuario getUsuarioId(@PathParam("id") Long id) {
        usuario u = usuarioServicio.getUsuario(id);
        if (u == null) throw new NotFoundException("Usuario no encontrado");
        return u;
    }

    // =====================================
    // Cambiar contraseña
    // =====================================
    @PUT
    @Path("/cambioContra/{id}")
    public String modContra(@PathParam("id") Long id, usuario usuario) {
        usuarioServicio.cambiarContrasenia(id, usuario);
        return "Contraseña modificada correctamente";
    }

    // =====================================
    // Obtener contraseña (no recomendado, pero lo mantengo)
    // =====================================
    @GET
    @Path("/getContra/{id}")
    public String getContra(@PathParam("id") Long id) {
        usuario u = usuarioServicio.getUsuario(id);
        if (u == null) throw new NotFoundException("Usuario no encontrado");
        return u.getContrasenia();
    }

    // =====================================
    // Login
    // =====================================
    @POST
    @Path("/login")
    public usuario login(LoginDTO credenciales) {
        usuario u = usuarioServicio.getUsuarioRegistrado(
                credenciales.email,
                credenciales.contrasenia
        );

        if (u == null) {
            throw new NotFoundException("Correo o contraseña incorrectos");
        }

        return u;
    }

    // =====================================
    // Actualizar usuario
    // =====================================
    @PUT
    @Path("/update/{id}")
    public usuario updateUsuario(@PathParam("id") Long id, UsuarioUpdateDTO usuarioUpdate) {
        return usuarioServicio.updateUsuario(id, usuarioUpdate);
    }

    // =====================================
    // Eliminar usuario
    // =====================================
    @DELETE
    @Path("/del/{id}")
    public String delUsuario(@PathParam("id") long id) {
        usuarioServicio.deleteUsuario(id);
        return "Se ha borrado exitosamente";
    }

    // =====================================
    // NUEVO: Enviar contraseña al correo del usuario
    // =====================================
    @POST
    @Path("/enviarContra/{id}")
    public String enviarContra(@PathParam("id") Long id) {

        usuario u = usuarioServicio.getUsuario(id);

        if (u == null) {
            throw new NotFoundException("Usuario no encontrado");
        }

        emailServicio.enviarContrasenia(u.getEmail(), u.getContrasenia());

        return "La contraseña fue enviada al correo: " + u.getEmail();
    }

    // =====================================
// Obtener ID del usuario por su email
// =====================================
    @GET
    @Path("/idPorEmail/{email}")
    public Long getIdPorEmail(@PathParam("email") String email) {

        usuario u = usuarioServicio.getUsuarioPorEmail(email);

        if (u == null) {
            throw new NotFoundException("No existe un usuario con el email: " + email);
        }

        return u.getId();
    }
}

