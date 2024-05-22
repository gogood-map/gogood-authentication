package gogood.authenticationJwt.controller;


import gogood.authenticationJwt.domain.usuario.DTO.UsuarioCriacaoDTO;
import gogood.authenticationJwt.domain.usuario.DTO.UsuarioNovo;
import gogood.authenticationJwt.domain.usuario.Usuario;
import gogood.authenticationJwt.service.usuario.UsuarioService;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioLoginDto;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> criar(@RequestBody @Valid UsuarioNovo usuarioNovo){

        if(this.usuarioService.emailExistente(usuarioNovo.email())){
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Email já cadastrado.");
        }
        this.usuarioService.criar(usuarioNovo);

        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto){
        UsuarioTokenDto usuarioToken = this.usuarioService.autenticar(usuarioLoginDto);
        System.out.println(usuarioToken);
        return ResponseEntity.status(200).body(usuarioToken);
    }

    @Operation(summary = "Obter todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados")
    @GetMapping
    public ResponseEntity<Iterable<Usuario>> get() {
        Iterable<Usuario> usuarios = usuarioService.get();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }





}
