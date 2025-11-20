package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdateDTO {
    @JsonProperty("elNombre")
    private String nombre;
    private String apellidos;
    private String cedula;
    private String email;
    private String telefono;
    private String direccionEntrega;
    private String imagen;
    private LocalDate fechaNacimiento;
}
