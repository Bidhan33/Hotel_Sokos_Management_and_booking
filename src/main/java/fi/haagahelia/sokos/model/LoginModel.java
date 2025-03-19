package fi.haagahelia.sokos.model;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginModel {

    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}