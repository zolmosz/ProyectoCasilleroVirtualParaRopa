package servicios;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailServicio {

    @Inject
    Mailer mailer;

    /**
     * Envía la contraseña al correo del usuario.
     */
    public void enviarContrasenia(String correo, String contrasenia) {

        String cuerpo = "Hola!\n\n"
                + "Tu contraseña registrada es:\n\n"
                + contrasenia + "\n\n"
                + "Si no solicitaste esta información, ignora este mensaje.\n\n"
                + "Upper";

        mailer.send(
                Mail.withText(
                        correo,
                        "Recuperación de Contraseña - UPPER",
                        cuerpo
                )
        );
    }
}
