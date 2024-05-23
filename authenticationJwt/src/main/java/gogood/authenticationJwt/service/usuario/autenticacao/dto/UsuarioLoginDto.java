package gogood.authenticationJwt.service.usuario.autenticacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioLoginDto {
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String senha;

}
