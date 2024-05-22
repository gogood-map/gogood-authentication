package gogood.authenticationJwt.mapper;

import gogood.authenticationJwt.domain.usuario.DTO.UsuarioNovo;
import gogood.authenticationJwt.domain.usuario.Usuario;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioCriacaoDTO;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioTokenDto;

public class UsuarioMapper {
    public static Usuario of(UsuarioNovo usuarioNovo){
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioNovo.email());
        usuario.setNome( usuarioNovo.nome());
        usuario.setSenha( usuarioNovo.senha());
        usuario.setGenero(usuarioNovo.genero());
        usuario.setDt_nascimento(usuarioNovo.dt_Nascimento());
        usuario.setGoogle_id(usuarioNovo.google_id());
        return usuario;
    }

    public static UsuarioTokenDto of(Usuario usuario, String token){
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();

        usuarioTokenDto.setUserId(usuario.getID());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setNome(usuario.getNome());
        usuarioTokenDto.setToken(token);

        return usuarioTokenDto;
    }
}
