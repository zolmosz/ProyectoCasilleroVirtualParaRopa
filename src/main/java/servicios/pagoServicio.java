package servicios;

import entidades.casillero;
import entidades.articulo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import dtos.PagoRequestDTO;
import entidades.pago;
import repositorios.pagoRepositorio;
import repositorios.casilleroRepositorio;
import repositorios.articuloRepositorio;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@AllArgsConstructor
public class pagoServicio {

    private final pagoRepositorio pagoRepositorio;
    private final casilleroRepositorio casilleroRepositorio;
    private final articuloRepositorio articuloRepositorio;

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
    //   PROCESAR PAGO
    // ======================
    @Transactional
    public pago procesarPago(PagoRequestDTO req, Long casilleroId, boolean persistir) {

        if (req == null) {
            throw new IllegalArgumentException("El cuerpo del pago no puede ser nulo.");
        }

        if (req.getMonto() == null || req.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del pago es inválido.");
        }

        // 1. OBTENER ARTÍCULOS PAGADOS ANTES DE CREAR LA ENTIDAD
        List<articulo> articulosPagados = List.of();
        if (req.getArticuloIds() != null && !req.getArticuloIds().isEmpty()) {
            articulosPagados = articuloRepositorio.list("id IN ?1", req.getArticuloIds());

            if (articulosPagados.size() != req.getArticuloIds().size()) {
                throw new IllegalArgumentException("Uno o más IDs de artículos proporcionados son inválidos.");
            }
        } else if (persistir) {
            throw new IllegalArgumentException("El pago debe contener al menos un artículo.");
        }

        pago p = new pago();

        // ... (ASIGNAR CAMPOS BASE, MASCARA TARJETA, REGLAS DE NEGOCIO, ASOCIAR CASILLERO sin cambios)

        String metodoNormalizado = req.getMetodo() == null
                ? null
                : req.getMetodo().trim().toUpperCase();

        p.setMetodo(metodoNormalizado);
        p.setMonto(req.getMonto());
        p.setNombre(req.getNombre() != null ? req.getNombre().trim() : null);
        p.setFecha(req.getFecha());

        String numeroOriginal = req.getNumeroTarjeta();

        if (numeroOriginal != null && numeroOriginal.length() > 4) {
            String last4 = numeroOriginal.substring(numeroOriginal.length() - 4);
            p.setNumeroTarjetaMask("**** **** **** " + last4);
        } else {
            p.setNumeroTarjetaMask(numeroOriginal);
        }

        String status = "APROBADO";
        String mensaje = "Pago procesado exitosamente.";

        if ("PSE".equalsIgnoreCase(req.getMetodo())) {
            status = "PENDIENTE";
            mensaje = "Pago pendiente por confirmar con plataforma PSE.";
        }

        if (numeroOriginal != null && numeroOriginal.endsWith("5")) {
            status = "RECHAZADO";
            mensaje = "La entidad bancaria rechazó el pago.";
        }

        p.setStatus(status);
        p.setMensaje(mensaje);

        casillero casilleroEncontrado = null;
        if (casilleroId != null) {
            casilleroEncontrado = casilleroRepositorio.findById(casilleroId);
            if (casilleroEncontrado != null) {
                p.setCasillero(casilleroEncontrado);
            }
        }

        // ======================
        //   ASOCIAR ARTÍCULOS PAGADOS
        // ======================
        // Esto crea las entradas en la tabla de asociación `pagos_articulos`.
        p.setArticulosPagados(articulosPagados);


        // ======================
        //   PERSISTIR Y MANEJAR INVENTARIO
        // ======================
        if (persistir) {
            pagoRepositorio.persistAndFlush(p); // 1. Persistir el pago y su relación

            if ("APROBADO".equalsIgnoreCase(status) && casilleroEncontrado != null && !articulosPagados.isEmpty()) {

                List<Long> idsPagados = articulosPagados.stream().map(a -> a.id).collect(Collectors.toList());

                // 🚨 CORRECCIÓN CLAVE: En lugar de eliminar, desvincular el artículo del casillero.
                // Esto mantiene el artículo en la BD para el historial de pagos, pero lo quita del casillero.

                // Opción 1 (JPA/Panache con consulta UPDATE directa):
                // Actualiza el casillero_id a NULL para los artículos pagados.
                articuloRepositorio.update("casillero = NULL where id IN ?1", idsPagados);

                // Opción 2 (Si la Opción 1 falla, usar la actualización de objetos):
                /*
                for (articulo a : articulosPagados) {
                    a.setCasillero(null);
                    articuloRepositorio.persist(a);
                }
                */

                // NOTA: No es necesario llamar a casilleroRepositorio.persist(casilleroEncontrado);
                // porque los cambios en su lista de artículos deben reflejarse a través de la actualización
                // del lado ManyToOne (la entidad 'articulo'). Si usas Panache,
                // la actualización directa con la consulta es más eficiente.
            }
        }

        return p;
    }

    // ======================
    //  SOLO SIMULAR
    // ======================
    public pago simularPago(PagoRequestDTO req, Long casilleroId) {
        return procesarPago(req, casilleroId, false);
    }
}