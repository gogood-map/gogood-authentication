package gogood.gogoodauthentication.controller;

import gogood.gogoodauthentication.DTO.Autenticacao;
import gogood.gogoodauthentication.DTO.Usuario;
import gogood.gogoodauthentication.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RequestMapping("/usuarios")
@RestController
public class UsuarioController {

    @Autowired
    UsuarioRepository repository ;

    @PostMapping()
    public ResponseEntity<Usuario> addUsuario(@RequestBody Usuario novoUsuario ){
        repository.save(novoUsuario);
        return ResponseEntity.ok().body(novoUsuario);
    }

    @GetMapping()
    public ResponseEntity<List<Usuario>> exibeTodos(){
        var lista = repository.findAll();
        return lista.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok().body(lista);
    }

    @GetMapping("/login")
    public ResponseEntity<Usuario> logar(@RequestBody Autenticacao entrada){
        System.out.println(entrada);
        if (repository.existsByEmail(entrada.getEntrada())){
            var user = repository.findByEmail(entrada.getEntrada());
            return entrada.getSenha().equals(user.getSenha()) ? ResponseEntity.ok().body(user) : ResponseEntity.status(403).build();
        }
        if (repository.existsByToken(entrada.getEntrada())){
            var user = repository.findByToken(entrada.getEntrada());
            return  ResponseEntity.ok().body(user);
        }
        return ResponseEntity.noContent().build();
    }

}
