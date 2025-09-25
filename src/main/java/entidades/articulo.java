package entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name="articulos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class articulo extends PanacheEntity{

    private long id_articulo;
    @JsonProperty("elNombre")
    private String nombre;
    private String talla;
    private String descripcion;
    private Long valorUnitario;
    @CreationTimestamp
    @JsonIgnore
    private LocalDate fechaCreacion;
    private String url;
}
