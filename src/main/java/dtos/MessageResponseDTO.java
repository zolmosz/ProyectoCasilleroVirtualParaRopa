package dtos;

public class MessageResponseDTO {
    public String mensaje;
    public String error;

    public MessageResponseDTO(String mensaje) {
        this.mensaje = mensaje;
    }

    public MessageResponseDTO(String mensaje, String error) {
        this.mensaje = mensaje;
        this.error = error;
    }

    public static MessageResponseDTO success(String mensaje) {
        return new MessageResponseDTO(mensaje);
    }

    public static MessageResponseDTO error(String error) {
        MessageResponseDTO dto = new MessageResponseDTO(null);
        dto.error = error;
        return dto;
    }
}
