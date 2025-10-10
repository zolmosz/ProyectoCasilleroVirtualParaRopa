package recursos;
import entidades.articulo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import servicios.usuarioServicio;
import entidades.usuario;
import dtos.LoginDTO;

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

    @DELETE
    @Path("/del/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delUsuario(@PathParam("id") long id) {
        this.usuarioServicio.deleteUsuario(id);
        return "Se ha borrado exitosamente";
    }
}