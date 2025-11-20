package recursos;
import entidades.articulo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import servicios.usuarioServicio;
import entidades.usuario;
import dtos.LoginDTO;
import dtos.UsuarioUpdateDTO;

import java.util.List;

@Path("/usuario")
@AllArgsConstructor
public class usuarioRecurso {
    private usuarioServicio usuarioServicio;

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public usuario addUsuario(usuario usuario) {
        return usuarioServicio.addUsuario(usuario);
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public List<usuario> getUsuarios() {
        return usuarioServicio.findAll();
    }

    @GET
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public usuario getUsuarioId(@PathParam("id") Long id) {
        return usuarioServicio.getUsuario(id);
    }


    @PUT
    @Path("/cambioContra/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void modContra(@PathParam("id") Long id, usuario usuario) {
        usuarioServicio.cambiarContrasenia(id, usuario);
    }

    @GET
    @Path("/getContra/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getContra(@PathParam("id") Long id) {
        return usuarioServicio.getUsuario(id).getContrasenia();
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public usuario login(LoginDTO credenciales) {
        usuario u = usuarioServicio.getUsuarioRegistrado(credenciales.email, credenciales.contrasenia);

        if (u == null) {
            throw new NotFoundException("Correo o contraseña incorrectos");
        }

        return u;
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public usuario updateUsuario(@PathParam("id") Long id, UsuarioUpdateDTO usuarioUpdate) {
        return usuarioServicio.updateUsuario(id, usuarioUpdate);
    }

    @DELETE
    @Path("/del/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delUsuario(@PathParam("id") long id) {
        this.usuarioServicio.deleteUsuario(id);
        return "Se ha borrado exitosamente";
    }

    /**
     * Endpoint para recuperar contraseña.
     * Envía la contraseña del usuario por correo electrónico.
     * 
     * @param email El correo electrónico del usuario (query parameter)
     * @return Response con mensaje de éxito o error
     */
    @POST
    @Path("/recuperar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recuperarContrasenia(@QueryParam("email") String email) {
        try {
            usuarioServicio.enviarContraseniaPorCorreo(email);
            return Response.ok().entity("{\"mensaje\": \"Correo de recuperación enviado\"}").build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        } catch (RuntimeException ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al enviar el correo\"}").build();
        }
    }
}
