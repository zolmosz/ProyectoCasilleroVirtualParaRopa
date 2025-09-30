package servicios;

import entidades.articulo;
import entidades.casillero;
import entidades.usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import repositorios.casilleroRepositorio;

@ApplicationScoped
public class casilleroServicio {
    @Inject
    casilleroRepositorio casilleroRepo;

    public casillero obtenerPorUsuario(Long usuarioId) {
        return casilleroRepo.find("usuario.id", usuarioId).firstResult();
    }

    @Transactional
    public casillero crearCasillero(usuario usuario) {
        if (obtenerPorUsuario(usuario.getId()) != null) {
            throw new IllegalStateException("El usuario ya tiene un casillero");
        }
        casillero nuevo = new casillero();
        nuevo.setUsuario(usuario);
        nuevo.setArticulos(new java.util.ArrayList<>());
        casilleroRepo.persist(nuevo);
        return nuevo;
    }

    @Transactional
    public casillero agregarArticulo(Long casilleroId, articulo art) {
        casillero c = casilleroRepo.findById(casilleroId);
        if (c == null) throw new IllegalArgumentException("Casillero no encontrado");
        if (!c.puedeAgregarArticulo(art)) {
            throw new IllegalStateException("No se puede agregar el artículo, excede el peso máximo de 10 libras");
        }
        c.getArticulos().add(art);
        casilleroRepo.persist(c);
        return c;
    }

    @Transactional
    public casillero quitarArticulo(Long casilleroId, Long articuloId) {
        casillero c = casilleroRepo.findById(casilleroId);
        if (c == null) throw new IllegalArgumentException("Casillero no encontrado");
        c.getArticulos().removeIf(a -> a.getId().equals(articuloId));
        casilleroRepo.persist(c);
        return c;
    }

    @Transactional
    public void eliminarCasillero(Long casilleroId) {
        casillero c = casilleroRepo.findById(casilleroId);
        if (c != null) {
            casilleroRepo.delete(c);
        }
    }
}
