package recursos;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import dtos.PagoRequestDTO;
import dtos.PagoResponseDTO;
import servicios.pagoServicio;
import entidades.pago;

@RequestScoped
@Path("/pagos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class pagorecurso {

    @Inject
    private pagoServicio pagoServicio;

    @POST
    @Path("/procesar")
    public Response procesarPago(PagoRequestDTO req, @QueryParam("persistir") @DefaultValue("true") boolean persistir) {
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
                    + " persistir: " + persistir);

            pago p = pagoServicio.procesarPago(req, persistir);

            PagoResponseDTO resp = new PagoResponseDTO(
                    p.getStatus(),
                    p.getMensaje(),
                    p.getMetodo(),
                    p.getMonto() != null ? p.getMonto().doubleValue() : null,
                    p.id,
                    p.getNumeroTarjetaMask(),
                    p.getNombre()
            );

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

    @GET
    public Response getAll() {
        List<pago> lista = pagoServicio.findAll();
        List<PagoResponseDTO> resp = lista.stream()
                .map(p -> new PagoResponseDTO(
                        p.getStatus(),
                        p.getMensaje(),
                        p.getMetodo(),
                        p.getMonto() != null ? p.getMonto().doubleValue() : null,
                        p.id,
                        p.getNumeroTarjetaMask(),
                        p.getNombre()
                ))
                .collect(Collectors.toList());
        return Response.ok(resp).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        pago p = pagoServicio.getPago(id);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Pago no encontrado"))
                    .build();
        }
        PagoResponseDTO resp = new PagoResponseDTO(
                p.getStatus(),
                p.getMensaje(),
                p.getMetodo(),
                p.getMonto() != null ? p.getMonto().doubleValue() : null,
                p.id,
                p.getNumeroTarjetaMask(),
                p.getNombre()
        );
        return Response.ok(resp).build();
    }
}