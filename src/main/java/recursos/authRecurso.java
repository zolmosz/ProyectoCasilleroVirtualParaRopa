package recursos;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import servicios.usuarioServicio;
import entidades.usuario;
import dtos.LoginDTO;

@Path("/login")
@AllArgsConstructor
public class authRecurso {
    private usuarioServicio usuarioServicio;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO credenciales) {
        if (credenciales == null || credenciales.email == null || credenciales.contrasenia == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("email and password are required")
                    .build();
        }
        usuario u = usuarioServicio.getUsuarioRegistrado(credenciales.email, credenciales.contrasenia);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Correo o contraseña incorrectos").build();
        }
        return Response.ok(u).build();
    }
}

