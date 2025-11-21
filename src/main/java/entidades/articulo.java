package entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime; // Usar LocalDateTime para sellos de tiempo más precisos

@Entity
@Table(name="articulos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class articulo extends PanacheEntity {

    // ==========================================
    // CORRECCIÓN: Evitar duplicidad de @JsonProperty
    // ==========================================
    // Si la entidad se usa en PagoResponseDTO, es mejor usar el nombre de campo
    // directamente, o que el DTO maneje el mapeo. Dejo @JsonProperty si es
    // necesario para peticiones entrantes.
    @JsonProperty("elNombre")
    private String nombre;

    private String talla;
    private String categoria;
    private String color;

    // ==========================================
    // MEJORA: Tipo para Precio
    // ==========================================
    // 'valorUnitario' es el precio. Se recomienda usar Long para evitar errores
    // de punto flotante en el dinero, manteniendo la consistencia con 'monto' en 'pago'.
    private Long valorUnitario;

    // ==========================================
    // MEJORA: Tipo de Sello de Tiempo
    // ==========================================
    // Se recomienda usar LocalDateTime o Instant para CreationTimestamp,
    // ya que LocalDate solo guarda la fecha sin hora.
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime fechaCreacion;

    private String url;

    private Double peso; // Peso en libras (Double es aceptable aquí)

    // ==========================================
    // RELACIÓN: Casillero (N-1)
    // ==========================================
    // Se añade FetchType.LAZY para evitar cargar el casillero cada vez que se carga un artículo.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "casillero_id")
    @JsonIgnore // Mantiene la funcionalidad de evitar ciclos y reducir payload
    private casillero casillero;

    // ==========================================
    // MEJORA: Getter para compatibilidad con DTO
    // ==========================================
    // Este getter facilita el mapeo de la entidad al ArticuloDTO que usa 'precio'.
    public Long getPrecio() {
        return this.valorUnitario;
    }
}