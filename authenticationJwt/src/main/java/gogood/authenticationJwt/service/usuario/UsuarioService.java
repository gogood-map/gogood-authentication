package gogood.authenticationJwt.service.usuario;

import gogood.authenticationJwt.api.configuration.security.JWT.GerenciadorTokenJwt;
import gogood.authenticationJwt.domain.Usuario.Usuario;
import gogood.authenticationJwt.domain.Usuario.repository.UsuarioRepository;
import gogood.authenticationJwt.service.usuario.DTO.UsuarioCriacaoDTO;
import gogood.authenticationJwt.service.usuario.DTO.UsuarioMepper;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioLoginDto;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;



    public void criar(UsuarioCriacaoDTO usuarioCriacaoDTO){
        final Usuario novoUsuario = UsuarioMepper.of(usuarioCriacaoDTO);

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        this.repository.save(novoUsuario);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto){
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(usuarioLoginDto.getEmail(),usuarioLoginDto.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado = repository.findByEmail(usuarioLoginDto.getEmail()).orElseThrow(
                () -> new ResponseStatusException(404,"Email não encontrado",null)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMepper.of(usuarioAutenticado, token);
    }
}
