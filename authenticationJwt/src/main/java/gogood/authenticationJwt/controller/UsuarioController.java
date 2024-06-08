package gogood.authenticationJwt.controller;


import gogood.authenticationJwt.domain.usuario.DTO.UsuarioAtualizado;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioNovoGoogle;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioNovo;
import gogood.authenticationJwt.domain.usuario.Usuarios;
import gogood.authenticationJwt.service.usuario.UsuarioService;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioLogin;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioLoginGoogle;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @Operation(summary = "Obter todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados")
    @GetMapping
    public ResponseEntity<Iterable<Usuarios>> get() {
        Iterable<Usuarios> usuarios = usuarioService.get();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }


    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioTokenDto> criar(@RequestBody @Valid UsuarioNovo usuarioNovo){

        if(this.usuarioService.emailExistente(usuarioNovo.email())){
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Email já cadastrado.");
        }
        this.usuarioService.criar(usuarioNovo);
        var usuarioNovoToken = this.usuarioService.autenticar(new UsuarioLogin(usuarioNovo.email(), usuarioNovo.senha()));
        return ResponseEntity.status(201).body(usuarioNovoToken);
    }

    @PostMapping("/cadastro-google")
    public ResponseEntity<Void> criarUsuarioGoogle(@RequestBody @Valid UsuarioNovoGoogle usuarioNovo){

        if(this.usuarioService.emailExistente(usuarioNovo.getEmail())){
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Email já cadastrado.");
        }
        this.usuarioService.criar(usuarioNovo);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLogin usuarioLogin){
        UsuarioTokenDto usuarioToken = this.usuarioService.autenticar(usuarioLogin);
        System.out.println(usuarioToken);
        return ResponseEntity.status(200).body(usuarioToken);
    }

    @PostMapping("/login-google")
    public ResponseEntity<UsuarioTokenDto> loginGoogle(@Valid @RequestBody UsuarioLoginGoogle usuarioLogin){
        UsuarioTokenDto usuarioToken = this.usuarioService.autenticar(usuarioLogin);
        System.out.println(usuarioToken);
        return ResponseEntity.status(200).body(usuarioToken);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable int id){
        usuarioService.delete(id);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioAtualizado> atualizarUsuario(@PathVariable int id, @RequestBody UsuarioAtualizado usuarioAtualizado){
        usuarioService.update(id, usuarioAtualizado);
        return ResponseEntity.status(204).build();
    }




}
