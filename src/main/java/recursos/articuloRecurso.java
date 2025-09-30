package recursos;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import servicios.articuloServicio;

import java.util.List;

@Path("/articulo")
@AllArgsConstructor
public class articuloRecurso {
    private articuloServicio articuloServicio;

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public articulo addProducto(articulo articulo) {
        return articuloServicio.addArticulo(articulo);
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public List<articulo> getArticulos() {
        return articuloServicio.findAll();
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
