package recursos;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import servicios.articuloServicio;
import entidades.articulo;

import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Path("/articulo")
@AllArgsConstructor
public class articuloRecurso {
    private articuloServicio articuloServicio;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProducto(articulo articulo) {
        try {
            articulo saved = articuloServicio.addArticulo(articulo);
            return Response.status(Response.Status.CREATED).entity(saved).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("details", e.getMessage()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            Throwable root = rootCause(e);
            Map<String, Object> details = new HashMap<>();
            details.put("error", e.toString());
            details.put("root", root != null ? root.toString() : null);
            details.put("message", root != null ? root.getMessage() : e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(details)
                    .build();
        }
    }

    private Throwable rootCause(Throwable t) {
        Throwable cur = t;
        while (cur != null && cur.getCause() != null && cur.getCause() != cur) {
            cur = cur.getCause();
        }
        return cur;
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public List<articulo> getArticulos() {
        return articuloServicio.findAll();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<articulo> searchArticulos(@QueryParam("q") String q,
                                          @QueryParam("categoria") String categoria) {
        boolean hasQ = q != null && !q.trim().isEmpty();
        boolean hasCat = categoria != null && !categoria.trim().isEmpty();
        if (!hasQ && !hasCat) {
            return articuloServicio.findAll();
        }
        if (hasQ && hasCat) {
            return articuloServicio.searchWithCategoria(q, categoria);
        }
        if (hasCat) {
            return articuloServicio.findByCategoria(categoria);
        }
        return articuloServicio.search(q);
    }

    @DELETE
    @Path("/del/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delArticulo(@PathParam("id") long id) {
        this.articuloServicio.deleteArticulo(id);
        return "Se ha borrado exitosamente";
    }

    @PUT
    @Path("/put/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void modArticulo(@PathParam("id") long id, articulo articulo) {
        articuloServicio.updateArticulo(id, articulo);
    }
}
