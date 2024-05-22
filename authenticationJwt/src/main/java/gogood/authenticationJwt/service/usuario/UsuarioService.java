package gogood.authenticationJwt.service.usuario;

import gogood.authenticationJwt.api.configuration.security.JWT.GerenciadorTokenJwt;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioAtualizado;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioNovo;
import gogood.authenticationJwt.domain.usuario.Usuario;
import gogood.authenticationJwt.repository.UsuarioRepository;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioCriacaoDTO;
import gogood.authenticationJwt.mapper.UsuarioMapper;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioLoginDto;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioTokenDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

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



    public void criar(UsuarioNovo usuarioNovo){
        final Usuario novoUsuario = UsuarioMapper.of(usuarioNovo);

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setCreated_at(LocalDate.now());
        this.repository.save(novoUsuario);
    }

    public UsuarioTokenDto autenticar(@Valid UsuarioLoginDto usuarioLoginDto){
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(usuarioLoginDto.getEmail(),usuarioLoginDto.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado = repository.findByEmail(usuarioLoginDto.getEmail()).orElseThrow(
                () -> new ResponseStatusException(404,"Email não encontrado",null)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);
    }


    public Iterable<Usuario> get() {
        Iterable<Usuario> usuarios = repository.findAll();
        return usuarios;
    }



    public Usuario delete(Integer id) {
        Optional<Usuario> usuario = repository.findById(id);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        repository.delete(usuario.get());

        return usuario.get();

    }
    public boolean emailExistente(String email){
        return repository.findByEmail(email).isPresent();
    }
    public Usuario update(String id, @Valid UsuarioAtualizado dadosAtualizacao) {
        Optional<Usuario> usuario = repository.findById(Integer.parseInt(id));
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        usuario.get().atualizar(dadosAtualizacao);
        repository.save(usuario.get());

        return usuario.get();
    }
}
