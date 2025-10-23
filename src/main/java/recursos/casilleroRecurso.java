package recursos;

import entidades.casillero;
import entidades.usuario;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import servicios.casilleroServicio;
import servicios.usuarioServicio;

import java.util.Collections;

@Path("/casillero")
@AllArgsConstructor
public class casilleroRecurso {
    private casilleroServicio casilleroServicio;
    private usuarioServicio usuarioServicio;

    @POST
    @Path("/create/{usuarioId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCasillero(@PathParam("usuarioId") Long usuarioId) {
        usuario u = usuarioServicio.getUsuario(usuarioId);
        if (u == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Usuario no encontrado").build();

        try {
            casillero c = casilleroServicio.crearCasillero(u);
            return Response.status(Response.Status.CREATED).entity(c).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear el casillero: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/get/{usuarioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCasilleroPorUsuario(@PathParam("usuarioId") Long usuarioId) {
        casillero c = casilleroServicio.obtenerPorUsuario(usuarioId);
        if (c == null) return Response.status(Response.Status.NOT_FOUND).entity("Casillero no encontrado").build();
        return Response.ok(c).build();
    }

    // Nuevo endpoint que devuelve únicamente el ID del casillero asociado al usuario
    @GET
    @Path("/id/{usuarioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCasilleroId(@PathParam("usuarioId") Long usuarioId) {
        casillero c = casilleroServicio.obtenerPorUsuario(usuarioId);
        if (c == null) return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("details", "Casillero no encontrado")).build();
        return Response.ok(Collections.singletonMap("casilleroId", c.id)).build();
    }
}
