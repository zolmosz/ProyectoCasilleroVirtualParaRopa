package entidades;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="casilleros")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class casillero extends PanacheEntity {

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private usuario usuario;

    @OneToMany
    @JoinColumn(name = "casillero_id")
    private List<articulo> articulos;

    public double getPesoTotal() {
        if (articulos == null) return 0.0;
        return articulos.stream().mapToDouble(a -> a.getPeso() != null ? a.getPeso() : 0.0).sum();
    }

    public boolean puedeAgregarArticulo(articulo art) {
        double pesoActual = getPesoTotal();
        double pesoNuevo = art.getPeso() != null ? art.getPeso() : 0.0;
        return (pesoActual + pesoNuevo) <= 10.0;
    }

    // Getter explícito para exponer el id heredado de PanacheEntity
    public Long getId() { return this.id; }
}
