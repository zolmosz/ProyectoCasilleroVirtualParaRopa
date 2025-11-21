package dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Nota: Eliminamos @AllArgsConstructor para que Lombok solo genere el constructor
// con los campos de clase (Long id, String nombre, Long precio),
// y definimos los constructores manualmente.

@Getter
@Setter
@NoArgsConstructor // Genera el constructor sin argumentos
public class ArticuloDTO {

    private Long id;
    private String nombre;

    // Cambiamos el tipo de campo a Long para mantener la consistencia
    // con la unidad de pago (centavos/pesos) de la entidad 'articulo'.
    private Long precio;

    // Constructor completo (usado para serialización/deserialización si todos los campos están presentes)
    public ArticuloDTO(Long id, String nombre, Long precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }
}