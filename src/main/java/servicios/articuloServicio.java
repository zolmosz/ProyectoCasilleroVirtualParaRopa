package servicios;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import repositorios.articuloRepositorio;
import repositorios.casilleroRepositorio;
import entidades.articulo;
import entidades.casillero;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class articuloServicio {
    private final articuloRepositorio articuloRepositorio;
    private final casilleroRepositorio casilleroRepositorio;

    // ======================
    //  CONSULTAS BÁSICAS
    // ======================
    public List<articulo> findAll() {
        return articuloRepositorio.listAll();
    }

    public List<articulo> findNombre(String nombre) {
        return articuloRepositorio.list("nombre", nombre);
    }

    public List<articulo> search(String q) {
        String filtro = "%" + q.toLowerCase() + "%";
        return articuloRepositorio.list(
                "lower(nombre) like ?1 or lower(color) like ?1 or lower(categoria) like ?1",
                filtro
        );
    }

    public List<articulo> findByCategoria(String categoria) {
        return articuloRepositorio.list("lower(categoria) = ?1", categoria.toLowerCase());
    }

    public List<articulo> searchWithCategoria(String q, String categoria) {
        String filtro = "%" + q.toLowerCase() + "%";
        return articuloRepositorio.list(
                "(lower(nombre) like ?1 or lower(color) like ?1 or lower(categoria) like ?1) and lower(categoria) = ?2",
                filtro,
                categoria.toLowerCase()
        );
    }

    public List<articulo> sortNombre() {
        return articuloRepositorio.listAll(Sort.by("nombre", Sort.Direction.Ascending));
    }

    public articulo getArticulo(Long id) {
        return articuloRepositorio.findById(id);
    }

    public List<articulo> findByCasillero(Long casilleroId) {
        return articuloRepositorio.list("casillero.id", casilleroId);
    }

    // ======================
    //  CREAR ARTÍCULO
    // ======================
    @Transactional
    public articulo addArticulo(articulo articulo) {
        if (articulo == null) {
            throw new IllegalArgumentException("El artículo no puede ser nulo");
        }
        if (articulo.getNombre() == null || articulo.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del artículo es obligatorio");
        }
        if (articulo.getCategoria() == null || articulo.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
        if (articulo.getValorUnitario() == null) {
            throw new IllegalArgumentException("El valorUnitario es obligatorio");
        }
        if (articulo.getUrl() == null || articulo.getUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("La URL de la imagen es obligatoria");
        }

        articuloRepositorio.persist(articulo);
        return articulo;
    }

    // ======================
    // CREAR ARTÍCULO EN CASILLERO
    // ======================
    @Transactional
    public articulo addArticuloToCasillero(Long casilleroId, articulo articulo) {
        casillero c = casilleroRepositorio.findById(casilleroId);
        if (c == null) {
            throw new IllegalArgumentException("Casillero no encontrado");
        }

        if (!c.puedeAgregarArticulo(articulo)) {
            throw new IllegalStateException("No se puede agregar el artículo, excede el peso máximo de 10 libras");
        }

        articulo.setCasillero(c);
        articuloRepositorio.persist(articulo);

        if (c.getArticulos() == null) {
            c.setArticulos(new java.util.ArrayList<>());
        }
        c.getArticulos().add(articulo);
        casilleroRepositorio.persist(c);

        return articulo;
    }

    // ======================
    // ELIMINAR ARTÍCULO
    // ======================
    @Transactional
    public void deleteArticulo(Long casilleroId, Long articuloId) {
        articulo art = articuloRepositorio.findById(articuloId);
        if (art == null) {
            throw new IllegalArgumentException("Artículo no encontrado");
        }
        if (art.getCasillero() == null || !art.getCasillero().id.equals(casilleroId)) {  // ✅ usa .id de PanacheEntity
            throw new IllegalArgumentException("El artículo no pertenece al casillero especificado");
        }

        articuloRepositorio.deleteById(articuloId);
    }

    // ======================
    // ACTUALIZAR ARTÍCULO
    // ======================
    @Transactional
    public articulo updateArticulo(Long casilleroId, Long articuloId, articulo datosActualizados) {
        articulo existente = articuloRepositorio.findById(articuloId);

        if (existente == null) {
            throw new IllegalArgumentException("No se encontró el artículo con id " + articuloId);
        }
        if (existente.getCasillero() == null || !existente.getCasillero().id.equals(casilleroId)) {  // ✅ usa id de PanacheEntity
            throw new IllegalArgumentException("El artículo no pertenece al casillero especificado");
        }

        existente.setNombre(datosActualizados.getNombre());
        existente.setTalla(datosActualizados.getTalla());
        existente.setCategoria(datosActualizados.getCategoria());
        existente.setColor(datosActualizados.getColor());
        existente.setValorUnitario(datosActualizados.getValorUnitario());
        existente.setUrl(datosActualizados.getUrl());
        existente.setPeso(datosActualizados.getPeso());

        return existente;
    }
}

