package repositorios;

import entidades.pago;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class pagoRepositorio implements PanacheRepository<pago> {
}