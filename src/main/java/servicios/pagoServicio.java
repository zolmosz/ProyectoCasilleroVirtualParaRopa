package servicios;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import dtos.PagoRequestDTO;
import entidades.pago;
import entidades.casillero;
import repositorios.pagoRepositorio;
import repositorios.casilleroRepositorio;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class pagoServicio {

    private final pagoRepositorio pagoRepositorio;
    private final casilleroRepositorio casilleroRepositorio;

    // ======================
    //  CONSULTAS BÁSICAS
    // ======================
    public List<pago> findAll() {
        return pagoRepositorio.listAll();
    }

    public pago getPago(Long id) {
        return pagoRepositorio.findById(id);
    }

    public List<pago> findByMetodo(String metodo) {
        return pagoRepositorio.list("metodo", metodo);
    }

    public List<pago> findByCasillero(Long casilleroId) {
        return pagoRepositorio.list("casillero.id", casilleroId);
    }

    // ======================
    //  PROCESAR (SIMULAR) Y OPCIONALMENTE PERSISTIR
    // ======================
    @Transactional
    public pago procesarPago(PagoRequestDTO req, boolean persistir) {
        if (req == null) {
            throw new IllegalArgumentException("Request de pago nulo");
        }
        if (req.getMonto() == null || req.getMonto() <= 0) {
            throw new IllegalArgumentException("Monto inválido");
        }

        // Depuración mínima
        System.out.println("[pagoServicio] procesarPago req -> metodo: " + req.getMetodo()
                + " nombre: " + req.getNombre() + " monto: " + req.getMonto());

        pago p = new pago();

        // Normalizar / limpiar inputs
        String metodoNormalized = req.getMetodo() == null ? null : req.getMetodo().trim().toLowerCase();
        p.setMetodo(metodoNormalized);

        // Adaptación simple: convierto Double a Long (ajusta según tu unidad)
        p.setMonto(req.getMonto().longValue());

        // Mascarar tarjeta: mantener últimos 4 dígitos
        String num = req.getNumeroTarjeta();
        if (num != null && num.length() > 4) {
            String last4 = num.substring(num.length() - 4);
            p.setNumeroTarjetaMask("**** **** **** " + last4);
        } else {
            p.setNumeroTarjetaMask(num);
        }

        // Guardar nombre (limpio)
        p.setNombre(req.getNombre() == null ? null : req.getNombre().trim());
        p.setFecha(req.getFecha());

        // lógica simulada
        String status = "APROBADO";
        String mensaje = "Pago procesado exitosamente";

        if ("pse".equalsIgnoreCase(req.getMetodo())) {
            status = "PENDIENTE";
            mensaje = "Pago pendiente por verificar con PSE";
        }

        if (num != null && num.endsWith("5")) {
            status = "RECHAZADO";
            mensaje = "La tarjeta fue rechazada por el banco";
        }

        p.setStatus(status);
        p.setMensaje(mensaje);

        // Asociar casillero si viene (comentado)
        // if (req.getCasilleroId() != null) {
        //     casillero c = casilleroRepositorio.findById(req.getCasilleroId());
        //     if (c != null) {
        //         p.setCasillero(c);
        //     }
        // }

        if (persistir) {
            // persistAndFlush para asegurar que la entidad tenga id antes de devolverla
            pagoRepositorio.persistAndFlush(p);
        }

        return p;
    }

    // Conveniencia: solo simular sin persistir
    public pago simularPago(PagoRequestDTO req) {
        return procesarPago(req, false);
    }
}