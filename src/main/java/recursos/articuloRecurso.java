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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class articuloRecurso {

    private final articuloServicio articuloServicio;

    // =========================
    // Agregar artículo a un casillero
    // =========================
    @POST
    @Path("/add/{casilleroId}")
    public Response addProducto(@PathParam("casilleroId") Long casilleroId, articulo articulo) {
        try {
            articulo result = articuloServicio.addArticuloToCasillero(casilleroId, articulo);
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al agregar el artículo").build();
        }
    }

    // =========================
    // Obtener todos los artículos
    // =========================
    @GET
    @Path("/get")
    public Response getArticulos() {
        try {
            List<articulo> articulos = articuloServicio.findAll();
            return Response.ok(articulos).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los artículos").build();
        }
    }

    // =========================
    // Obtener artículos por casillero
    // =========================
    @GET
    @Path("/get/{casilleroId}")
    public Response getArticulosPorCasillero(@PathParam("casilleroId") Long casilleroId) {
        try {
            List<articulo> articulos = articuloServicio.findByCasillero(casilleroId);
            return Response.ok(articulos).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener los artículos del casillero").build();
        }
    }

    // =========================
    // Eliminar un artículo
    // =========================
    @DELETE
    @Path("/del/{casilleroId}/{articuloId}")
    public Response delArticulo(
            @PathParam("casilleroId") Long casilleroId,
            @PathParam("articuloId") Long articuloId) {
        try {
            articuloServicio.deleteArticulo(casilleroId, articuloId);
            return Response.ok("Artículo eliminado correctamente").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar el artículo").build();
        }
    }

    // =========================
    // Modificar un artículo
    // =========================
    @PUT
    @Path("/put/{casilleroId}/{articuloId}")
    public Response modArticulo(
            @PathParam("casilleroId") Long casilleroId,
            @PathParam("articuloId") Long articuloId,
            articulo articulo) {
        try {
            articuloServicio.updateArticulo(casilleroId, articuloId, articulo);
            return Response.ok("Artículo actualizado correctamente").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al modificar el artículo").build();
        }
    }
}

