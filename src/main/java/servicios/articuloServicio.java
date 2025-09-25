package servicios;
import entidades.articulo;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import repositorios.articuloRepositorio;

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
    public List<articulo> findNombreDescripcion(String nombre, String descripcion) {
        String filter1 = '%'+nombre+'%';
        String filter2 = '%'+descripcion+'%';
        return this.articuloRepositorio.list("nombre ILIKE ?1 or descripcion ILIKE ?2",filter1, filter2);
    }
    public List<articulo> sortNombre() {
        return this.articuloRepositorio.listAll(Sort.by("nombre",Sort.Direction.Ascending));
    }
    public articulo getArticulo(Long id) {
        return this.articuloRepositorio.findById(id);
    }

    @Transactional
    public articulo addArticulo(articulo articulo) {
        this.articuloRepositorio.persist(articulo);
        return articulo;
    }
    @Transactional
    public articulo updateArticulo(long id,articulo articulo) {
        this.articuloRepositorio.persist(articulo);
        return articulo;
    }
    @Transactional
    public void deleteArticulo(long id) {
        this.articuloRepositorio.deleteById(id);
    }
}
