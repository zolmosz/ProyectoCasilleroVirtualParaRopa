package recursos;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import servicios.articuloServicio;
import entidades.articulo;

import java.util.List;

@Path("/articulo")
@AllArgsConstructor
public class articuloRecurso {
    private articuloServicio articuloServicio;

    @POST
    @Path("/add/{casilleroId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProducto(@PathParam("casilleroId") Long casilleroId, articulo articulo) {
        try {
            articulo result = articuloServicio.addArticuloToCasillero(casilleroId, articulo);
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public List<articulo> getArticulos() {
        return articuloServicio.findAll();
    }

    @GET
    @Path("/get/{casilleroId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<articulo> getArticulosPorCasillero(@PathParam("casilleroId") Long casilleroId) {
        return articuloServicio.findByCasillero(casilleroId);
    }

    @DELETE
    @Path("/del/{casilleroId}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delArticulo(@PathParam("casilleroId") Long casilleroId, @PathParam("id") long id) {
        try {
            articuloServicio.deleteArticulo(casilleroId, id);
            return Response.ok("Se ha borrado exitosamente").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/put/{casilleroId}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response modArticulo(@PathParam("casilleroId") Long casilleroId, @PathParam("id") long id, articulo articulo) {
        try {
            articuloServicio.updateArticulo(casilleroId, id, articulo);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
