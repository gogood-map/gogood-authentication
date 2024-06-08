package gogood.authenticationJwt.service.usuario;

import gogood.authenticationJwt.api.configuration.security.JWT.GerenciadorTokenJwt;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioAtualizado;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioNovo;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioNovoGoogle;
import gogood.authenticationJwt.domain.usuario.Usuarios;
import gogood.authenticationJwt.excecoes.RecursoNaoEncontradoException;
import gogood.authenticationJwt.excecoes.RecursoVazioException;
import gogood.authenticationJwt.repository.UsuarioRepository;
import gogood.authenticationJwt.mapper.UsuarioMapper;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioLogin;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioLoginGoogle;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioTokenDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
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
        final Usuarios novoUsuarios = UsuarioMapper.of(usuarioNovo);

        String senhaCriptografada = passwordEncoder.encode(novoUsuarios.getSenha());
        novoUsuarios.setSenha(senhaCriptografada);
        novoUsuarios.setCreated_at(LocalDate.now());
        this.repository.save(novoUsuarios);
    }

    public void criar(UsuarioNovoGoogle usuarioNovo){
        final Usuarios novoUsuarios = UsuarioMapper.of(usuarioNovo);
        String senhaCriptografada = passwordEncoder.encode(usuarioNovo.getGoogle_id());
        novoUsuarios.setSenha(senhaCriptografada);

        novoUsuarios.setCreated_at(LocalDate.now());
        this.repository.save(novoUsuarios);
    }

    public UsuarioTokenDto autenticar(@Valid UsuarioLogin usuarioLogin){
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(usuarioLogin.getEmail(), usuarioLogin.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuarios usuariosAutenticado = repository.findByEmail(usuarioLogin.getEmail()).orElseThrow(
                () -> new ResponseStatusException(404,"Email não encontrado",null)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuariosAutenticado, token);
    }

    public UsuarioTokenDto autenticar(@Valid UsuarioLoginGoogle usuarioLoginGoogle){
        var consulta = repository.buscarGoogleId(usuarioLoginGoogle.getGoogle_id());
        if(consulta.isEmpty()){
            throw new ResponseStatusException(HttpStatusCode.valueOf(401), "Login não autorizado");
        }

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(usuarioLoginGoogle.getEmail(), usuarioLoginGoogle.getGoogle_id());


        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuarios usuarioAutenticado = repository.buscarPeloGoogleId(usuarioLoginGoogle.getGoogle_id()).get();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);
    }


    public Iterable<Usuarios> get() {
        List<Usuarios> usuarios = repository.findAll();
        if (usuarios.isEmpty()) throw new RecursoVazioException("Lista de usuários vazia.");
        return usuarios;
    }



    public void delete(Integer id) {
        Optional<Usuarios> usuario = repository.findById(id);
        if (usuario.isEmpty()) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }

        repository.delete(usuario.get());
    }
    public boolean emailExistente(String email){
        return repository.findByEmail(email).isPresent();
    }
    public void update(int id, @Valid UsuarioAtualizado dadosAtualizacao) {
        Optional<Usuarios> usuario = repository.findById(id);
        if (usuario.isEmpty()) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }

        usuario.get().atualizar(dadosAtualizacao);
        repository.save(usuario.get());

    }
}
