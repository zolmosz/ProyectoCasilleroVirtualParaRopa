package recursos;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dtos.PagoRequestDTO;
import dtos.PagoResponseDTO;
import dtos.ArticuloDTO;
import servicios.pagoServicio;
import entidades.pago;
import entidades.articulo;

@RequestScoped
@Path("/pagos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class pagorecurso {

    @Inject
    private pagoServicio pagoServicio;

    // ==========================================
    // MÉTODO DE MAPEO: ENTIDAD PAGO A PAGORESPONSE DTO
    // ==========================================
    /**
     * Convierte la entidad 'pago' en el DTO de respuesta,
     * incluyendo la lista de Artículos pagados con su URL de imagen.
     */
    private PagoResponseDTO toResponseDTO(pago p) {

        // Mapear la lista de entidades Articulo a una lista de ArticuloDTO
        List<ArticuloDTO> articulosDto = p.getArticulosPagados().stream()
                // ArticuloDTO ahora requiere 4 argumentos: id, nombre, precio, imagen.
                .map(art -> new ArticuloDTO(
                        art.id,
                        art.getNombre(),
                        art.getPrecio(),
                        art.getUrl() // 💡 CORRECCIÓN CLAVE: Mapear el campo 'url' de la entidad al DTO
                ))
                .collect(Collectors.toList());

        // Retornar el PagoResponseDTO con el constructor actualizado
        return new PagoResponseDTO(
                p.getStatus(),
                p.getMensaje(),
                p.getMetodo(),
                p.getMonto(),
                p.id,
                p.getNumeroTarjetaMask(),
                p.getNombre(),
                articulosDto
        );
    }

    // ======================
    // PROCESAR PAGO
    // ======================
    @POST
    @Path("/procesar/{casilleroId}")
    public Response procesarPago(
            PagoRequestDTO req,
            @PathParam("casilleroId") Long casilleroId,
            @QueryParam("persistir") @DefaultValue("true") boolean persistir) {

        try {
            if (req == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Request vacío")).build();
            }
            if (req.getMonto() == null || req.getMonto() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Monto inválido")).build();
            }

            // Logging para depuración
            System.out.println("[PagoRecurso] recibí request -> metodo: " + req.getMetodo()
                    + " nombre: " + req.getNombre()
                    + " monto: " + req.getMonto()
                    + " casilleroId: " + casilleroId
                    + " persistir: " + persistir);

            // Llamada al servicio
            pago p = pagoServicio.procesarPago(req, casilleroId, persistir);

            // Mapeo usando la nueva función
            PagoResponseDTO resp = toResponseDTO(p);

            if (persistir) {
                return Response.status(Response.Status.CREATED).entity(resp).build();
            } else {
                return Response.ok(resp).build();
            }

        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", ex.getMessage()))
                    .build();
        } catch (Exception ex) {
            System.err.println("[PagoRecurso] error procesando pago: " + ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error procesando el pago"))
                    .build();
        }
    }

    // ======================
    // OBTENER TODOS LOS PAGOS
    // ======================
    @GET
    public Response getAll() {
        List<pago> lista = pagoServicio.findAll();
        List<PagoResponseDTO> resp = lista.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return Response.ok(resp).build();
    }

    // ======================
    // OBTENER PAGO POR ID
    // ======================
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        pago p = pagoServicio.getPago(id);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Pago no encontrado"))
                    .build();
        }

        // Mapeo usando la nueva función
        PagoResponseDTO resp = toResponseDTO(p);

        return Response.ok(resp).build();
    }
}