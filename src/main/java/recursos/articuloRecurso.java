package recursos;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import servicios.articuloServicio;
import entidades.articulo;
import servicios.usuarioServicio;
import servicios.casilleroServicio;
import entidades.usuario;
import entidades.casillero;

import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/articulo")
@AllArgsConstructor
public class articuloRecurso {
    private articuloServicio articuloServicio;
    private usuarioServicio usuarioServicio;
    private casilleroServicio casilleroServicio;
    private static final Logger LOG = Logger.getLogger(articuloRecurso.class.getName());

    @POST
    @Path("/add/{casilleroId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProducto(@PathParam("casilleroId") Long casilleroId, articulo articulo) {
        try {
            articulo saved = articuloServicio.addArticuloToCasillero(casilleroId, articulo);
            return Response.status(Response.Status.CREATED).entity(saved).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("details", e.getMessage()))
                    .build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error adding articulo", e);
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

    // Nuevo endpoint: recibe usuarioId, busca/crea el casillero y agrega el artículo ahí
    @POST
    @Path("/addByUsuario/{usuarioId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProductoByUsuario(@PathParam("usuarioId") Long usuarioId, articulo articulo) {
        try {
            if (usuarioId == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("details", "usuarioId is required")).build();
            }
            usuario u = usuarioServicio.getUsuario(usuarioId);
            if (u == null) {
                return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("details", "Usuario no encontrado")).build();
            }
            casillero c = casilleroServicio.obtenerPorUsuario(usuarioId);
            if (c == null) {
                // crear casillero si no existe
                c = casilleroServicio.crearCasillero(u);
            }
            articulo saved = articuloServicio.addArticuloToCasillero(c.id, articulo);
            Map<String, Object> resp = new HashMap<>();
            resp.put("articulo", saved);
            resp.put("casilleroId", c.id);
            return Response.status(Response.Status.CREATED).entity(resp).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("details", e.getMessage()))
                    .build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error adding articulo by usuario", e);
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
    public Response getArticulos(@QueryParam("casilleroId") Long casilleroId) {
        if (casilleroId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("details", "casilleroId query param is required"))
                    .build();
        }
        List<articulo> list = articuloServicio.findByCasillero(casilleroId);
        return Response.ok(list).build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchArticulos(@QueryParam("q") String q,
                                          @QueryParam("categoria") String categoria,
                                          @QueryParam("casilleroId") Long casilleroId) {
        if (casilleroId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("details", "casilleroId query param is required"))
                    .build();
        }
        boolean hasQ = q != null && !q.trim().isEmpty();
        boolean hasCat = categoria != null && !categoria.trim().isEmpty();

        List<articulo> base = articuloServicio.findByCasillero(casilleroId);
        if (!hasQ && !hasCat) return Response.ok(base).build();
        // aplicar filtros en memoria (por simplicidad)
        List<articulo> filtered = base.stream().filter(a -> {
            if (hasQ) {
                String f = q.toLowerCase();
                boolean matchQ = (a.getNombre() != null && a.getNombre().toLowerCase().contains(f)) ||
                        (a.getColor() != null && a.getColor().toLowerCase().contains(f)) ||
                        (a.getCategoria() != null && a.getCategoria().toLowerCase().contains(f));
                if (!matchQ) return false;
            }
            if (hasCat) {
                if (a.getCategoria() == null || !a.getCategoria().equalsIgnoreCase(categoria)) return false;
            }
            return true;
        }).toList();

        return Response.ok(filtered).build();
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
