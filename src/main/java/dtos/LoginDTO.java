package dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

public class LoginDTO {
    public String email;
    @JsonAlias({"contrasenia", "password"})
    public String contrasenia;
}
