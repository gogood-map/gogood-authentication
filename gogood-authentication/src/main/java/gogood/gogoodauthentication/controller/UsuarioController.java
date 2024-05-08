package gogood.gogoodauthentication.controller;

import gogood.gogoodauthentication.DTO.Autenticacao;
import gogood.gogoodauthentication.DTO.Usuario;
import gogood.gogoodauthentication.repository.UsuarioRepository;
import gogood.gogoodauthentication.security.TokenService;
import gogood.gogoodauthentication.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private Token token;

    @GetMapping()
    public ResponseEntity<List<Usuario>> exibeTodos(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("Chamou exibe");
        var lista = repository.findAll();
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(lista);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> addUsuario(@RequestBody Usuario novoUsuario) {
        repository.save(novoUsuario);
        return ResponseEntity.ok().body(novoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity logar(@RequestBody Autenticacao entrada) {
        System.out.println(entrada);
        if (repository.existsByLogin(entrada.getEntrada())) {
            System.out.println("Entrou email");
            var user = repository.findByLogin(entrada.getEntrada());
            return entrada.getSenha().equals(user.getPassword()) ? ResponseEntity.ok().body(token.getToken((Usuario) user)) : ResponseEntity.status(403).build();
        }
        if (repository.existsByToken(entrada.getEntrada())) {
            System.out.println("entrou token");
            var user = repository.findByToken(entrada.getEntrada());
            return ResponseEntity.ok().body(token.getToken(user));
        }
        return ResponseEntity.noContent().build();
    }
}
