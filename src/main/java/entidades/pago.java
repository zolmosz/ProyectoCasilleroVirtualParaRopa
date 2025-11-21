package entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
// Usamos PanacheEntity. No es necesario el getter para 'id' si solo usamos el heredado.
public class pago extends PanacheEntity {

    @JsonProperty("metodoPago")
    private String metodo;

    // Monto en unidad definida (centavos / pesos)
    // Se mantiene como Long para evitar errores de punto flotante en el dinero.
    private Long monto;

    // Solo últimos 4 dígitos o máscara
    private String numeroTarjetaMask;

    private String nombre;

    // Fecha asociada al método/tarjeta (string flexible)
    private String fecha;

    private String status;
    private String mensaje;

    // ==========================
    // RELACIÓN: CASILLERO (N-1)
    // ==========================
    // Se añade FetchType.LAZY para evitar cargar el casillero innecesariamente
    // cuando solo se consulta el pago.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "casillero_id")
    @JsonIgnore // Mantiene la funcionalidad de evitar ciclos y reducir payload
    private casillero casillero;

    // ==========================================
    // RELACIÓN: ARTÍCULOS QUE FUERON PAGADOS
    // ==========================================
    // Se añade FetchType.LAZY. Los artículos pagados solo se necesitan al ver
    // el historial detallado, no en una lista de pagos.
    // cascade = CascadeType.ALL no es necesario aquí, ya que el pago no gestiona
    // el ciclo de vida del artículo, solo lo referencia.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "pagos_articulos",
            joinColumns = @JoinColumn(name = "pago_id"),
            inverseJoinColumns = @JoinColumn(name = "articulo_id")
    )
    private List<articulo> articulosPagados = new ArrayList<>();

    // NOTA: Se recomienda inicializar las colecciones en el constructor por defecto
    // o con el asignador, ya que Panache/Hibernate puede usar un constructor sin
    // argumentos. Tu código ya lo hace con el asignador ( = new ArrayList<>(); )

}