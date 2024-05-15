package gogood.authenticationJwt.service.usuario.DTO;

import gogood.authenticationJwt.domain.Usuario.Usuario;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioTokenDto;

public class UsuarioMepper {
    public static Usuario of(UsuarioCriacaoDTO usuarioCriacaoDTO){
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioCriacaoDTO.getEmail());
        usuario.setNome( usuarioCriacaoDTO.getNome());
        usuario.setSenha( usuarioCriacaoDTO.getSenha());
        usuario.setGenero(usuarioCriacaoDTO.getGenero());
        usuario.setDataNascimento(usuarioCriacaoDTO.getDataNascimento());
        usuario.setToken(usuarioCriacaoDTO.getToken());
        return usuario;
    }

    public static UsuarioTokenDto of(Usuario usuario, String token){
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();

        usuarioTokenDto.setUserId(usuario.getId());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setNome(usuario.getNome());
        usuarioTokenDto.setToken(usuario.getToken());

        return usuarioTokenDto;
    }
}
