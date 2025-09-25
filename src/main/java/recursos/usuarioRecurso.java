package recursos;
import entidades.articulo;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import servicios.usuarioServicio;
import entidades.usuario;

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
    @Path("/get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public usuario getUsuarioId(@PathParam("id") long id) {
        return usuarioServicio.getUsuario(id) ;
    }

    @PUT
    @Path("/cambioContra/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void modArticulo(@PathParam("id") long id, usuario usuario) {
        var usuarioActualizado = usuarioServicio.getUsuario(id);
        if (usuarioActualizado != null) {
            usuarioActualizado.setContrasenia(usuario.getContrasenia());
            usuarioServicio.cambiarContrasenia(id,usuarioActualizado);
        }
    }

}
