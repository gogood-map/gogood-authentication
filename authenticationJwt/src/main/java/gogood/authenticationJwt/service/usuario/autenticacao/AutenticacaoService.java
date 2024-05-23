package gogood.authenticationJwt.service.usuario.autenticacao;


import gogood.authenticationJwt.domain.usuario.Usuario;
import gogood.authenticationJwt.repository.UsuarioRepository;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuariosDetalhesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(username);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("usuario: %s nao encontrado", username));
        }
        return new UsuariosDetalhesDto(usuarioOpt.get());
    }
}
