package entidades;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name="usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class usuario extends PanacheEntity{
    @JsonProperty("elNombre")
    private String nombre;
    private String apellidos;
    private String cedula;
    private String email;
    private String telefono;
    private String contrasenia;

    @JsonIgnore
    private LocalDate fechaNacimiento;
}
