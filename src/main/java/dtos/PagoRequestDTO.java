package dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List; // Importación necesaria para List

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagoRequestDTO {

    @JsonProperty("metodoPago")
    private String metodo;

    // Mantener consistente con la entidad pago (Long)
    // Debe ser enviado en la unidad más pequeña (ej. centavos)
    private Long monto;

    // Número de tarjeta completo (solo request, nunca persistir)
    private String numeroTarjeta;

    @JsonProperty("elNombre")
    private String nombre;

    private String fecha;

    // Solo para validación; jamás se guarda en BD
    private String cvv;

    // ===============================================
    // CAMBIO CLAVE: IDs de los artículos que se pagan
    // ===============================================
    /**
     * Lista de IDs de los artículos que el usuario desea pagar.
     * Estos IDs se usarán en el servicio para asociar el pago a los artículos
     * y quitarlos del casillero.
     */
    private List<Long> articuloIds;
}