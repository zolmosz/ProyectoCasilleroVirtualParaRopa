package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagoRequestDTO {
    @JsonProperty("metodoPago")
    private String metodo;
    private Double monto;
    private String numeroTarjeta;
    @JsonProperty("elNombre")
    private String nombre;
    private String fecha;
    // Recibir para la simulación; NO persistir en BD
    private String cvv;
}