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
public class PagoResponseDTO {
    private String status;
    private String mensaje;
    private String metodo;
    private Double monto;
    private Long id;
    // máscara o últimos 4 dígitos (si quieres exponerlo)
    private String numeroTarjetaMask;

    @JsonProperty("nombre")
    private String nombre;
}