package gogood.authenticationJwt.domain.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioNovo;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioAtualizado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer ID;
    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dt_nascimento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDate created_at;
    private String genero;
    private String google_id;




    public Usuario(UsuarioNovo usuarioNovo) {
        this.nome = usuarioNovo.nome();
        this.email = usuarioNovo.email();
        this.senha = usuarioNovo.senha();
        this.genero = usuarioNovo.genero();
        this.dt_nascimento = usuarioNovo.dt_Nascimento();
        this.google_id = usuarioNovo.google_id();
        this.created_at = LocalDate.now();
    }



    public void atualizar(UsuarioAtualizado usuarioAtualizado){
        this.nome = usuarioAtualizado.nome();
        this.email = usuarioAtualizado.email();
        this.genero = usuarioAtualizado.genero();
        this.dt_nascimento = usuarioAtualizado.dt_Nascimento();
    }
}