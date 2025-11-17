package entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class pago extends PanacheEntity {

    @JsonProperty("metodoPago")
    private String metodo;

    // monto en la unidad que prefieras (ej. centavos / pesos)
    private Long monto;

    // almacenar sólo la máscara o últimos 4 dígitos
    private String numeroTarjetaMask;

    private String nombre;

    // fecha asociada al pago o tarjeta (cadena para formato libre)
    private String fecha;

    private String status;
    private String mensaje;
}