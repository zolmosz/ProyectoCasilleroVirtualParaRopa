package dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // Genera el constructor sin argumentos
public class ArticuloDTO {

    private Long id;
    private String nombre;
    private Long precio;

    // 💡 CAMBIO CLAVE: Usamos 'imagen' en el DTO, que será llenado con el campo 'url' de la Entidad.
    private String imagen;

    // Constructor completo
    public ArticuloDTO(Long id, String nombre, Long precio, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen; // Campo de la URL de la imagen
    }
}