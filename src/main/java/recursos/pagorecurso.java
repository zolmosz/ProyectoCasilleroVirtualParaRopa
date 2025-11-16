package recursos;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pagos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class pagorecurso {

    @POST
    @Path("/procesar")
    public Response procesarPago(PagoRequest request) {

        // Validación básica
        if (request == null || request.metodo == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PagoResponse("RECHAZADO", "Datos incompletos", null, 0))
                    .build();
        }

        // Simulación de lógica real
        String status = "APROBADO";
        String mensaje = "Pago procesado exitosamente";

        // Reglas de ejemplo:
        if ("pse".equalsIgnoreCase(request.metodo)) {
            status = "PENDIENTE";
            mensaje = "Pago pendiente por verificar con PSE";
        }

        if (request.numeroTarjeta != null && request.numeroTarjeta.endsWith("5")) {
            status = "RECHAZADO";
            mensaje = "La tarjeta fue rechazada por el banco";
        }

        PagoResponse resp = new PagoResponse(
                status,
                mensaje,
                request.metodo,
                request.monto
        );

        return Response.ok(resp).build();
    }

    // ===============================
    // CLASES INTERNAS DEL RECURSO
    // ===============================

    public static class PagoRequest {
        public String metodo;
        public double monto;
        public String numeroTarjeta;
        public String nombre;
        public String fecha;
        public String cvv;
    }

    public static class PagoResponse {
        public String status;
        public String mensaje;
        public String metodo;
        public double monto;

        public PagoResponse() {}

        public PagoResponse(String status, String mensaje, String metodo, double monto) {
            this.status = status;
            this.mensaje = mensaje;
            this.metodo = metodo;
            this.monto = monto;
        }
    }
}
