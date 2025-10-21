package servicios;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import repositorios.articuloRepositorio;
import entidades.articulo;

import java.util.List;

@ApplicationScoped
@AllArgsConstructor
public class articuloServicio {
    private articuloRepositorio articuloRepositorio;

    public List<articulo> findAll() {
        return this.articuloRepositorio.listAll();
    }
    public List<articulo> findNombre(String nombre) {
        return this.articuloRepositorio.list("nombre",nombre);
    }

    // Busca por nombre, color o categoria (todas String en minúsculas)
    public List<articulo> search(String q) {
        String filtro = "%" + q.toLowerCase() + "%";
        return this.articuloRepositorio.list(
                "lower(nombre) like ?1 or lower(color) like ?1 or lower(categoria) like ?1",
                filtro
        );
    }

    // Buscar solo por categoría exacta (case-insensitive)
    public List<articulo> findByCategoria(String categoria) {
        return this.articuloRepositorio.list("lower(categoria) = ?1", categoria.toLowerCase());
    }

    // Buscar por texto y filtrar por categoría exacta (case-insensitive)
    public List<articulo> searchWithCategoria(String q, String categoria) {
        String filtro = "%" + q.toLowerCase() + "%";
        return this.articuloRepositorio.list(
                "(lower(nombre) like ?1 or lower(color) like ?1 or lower(categoria) like ?1) and lower(categoria) = ?2",
                filtro,
                categoria.toLowerCase()
        );
    }

    public List<articulo> sortNombre() {
        return this.articuloRepositorio.listAll(Sort.by("nombre",Sort.Direction.Ascending));
    }
    public articulo getArticulo(long id) {
        return this.articuloRepositorio.findById(id);
    }

    @Transactional
    public articulo addArticulo(articulo articulo) {
        // Validaciones básicas
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
        this.articuloRepositorio.persist(articulo);
        return articulo;
    }

    @Transactional
    public void deleteArticulo(long id) {
        this.articuloRepositorio.deleteById(id);
    }

    @Transactional
    public articulo updateArticulo(long id, articulo datosActualizados) {
        articulo existente = this.articuloRepositorio.findById(id);

        if (existente == null) {
            throw new RuntimeException("No se encontró el artículo con id " + id);
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
