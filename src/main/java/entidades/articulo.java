package entidades;
import com.fasterxml.jackson.annotation.*;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name="articulos")
@AttributeOverride(name = "id", column = @Column(name = "id_articulo"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class articulo extends PanacheEntity {
    @JsonProperty("elNombre")
    @JsonAlias({"elNombre", "nombre"})
    private String nombre;
    private String talla;

    private String categoria;

    private Long valorUnitario;
    @CreationTimestamp
    @JsonIgnore
    private LocalDate fechaCreacion;
    @Column(columnDefinition = "text")
    private String url;
    private Double peso;
    private String color;

    // Relación ManyToOne hacia casillero: cada artículo pertenece a un único casillero
    @ManyToOne
    @JoinColumn(name = "casillero_id")
    @JsonIgnoreProperties({"articulos", "usuario"})
    private casillero casillero;

    // Getter explícito por compatibilidad con código que usa getId()
    public Long getId() {
        return this.id;
    }
}
