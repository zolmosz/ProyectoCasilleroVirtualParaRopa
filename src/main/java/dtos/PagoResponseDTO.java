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
public class PagoResponseDTO {

    private String status;
    private String mensaje;

    // Método de pago (coincide con el request y con la entidad)
    private String metodo;

    // Monto pagado — debe ser Long para coincidir con la entidad
    private Long monto;

    // ID del pago generado en BD
    private Long id;

    // Número de tarjeta enmascarado (ej: **** **** **** 1234)
    private String numeroTarjetaMask;

    // Nombre del titular
    @JsonProperty("nombre")
    private String nombre;

    // ===============================================
    // CAMBIO CLAVE: Artículos asociados a este pago
    // ===============================================
    /**
     * Lista de los artículos que fueron incluidos en esta transacción.
     * Se usa un DTO simple (ArticuloDTO) para evitar la sobrecarga de datos.
     */
    private List<ArticuloDTO> articulosPagados;

    // NOTA: El constructor @AllArgsConstructor debe ser regenerado
    // o actualizado para incluir este nuevo campo si estás usando Lombok.
}