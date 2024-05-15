package gogood.authenticationJwt.service.usuario.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioCriacaoDTO {
    private String token;
    private String email;

    private String senha;

    @NotBlank
    private String nome;
    @NotBlank
    private String genero;
    @NotNull
    private LocalDate dataNascimento;

}
