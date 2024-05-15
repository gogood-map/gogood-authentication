package gogood.authenticationJwt.api.controller.usuario;

import gogood.authenticationJwt.domain.Usuario.Usuario;
import gogood.authenticationJwt.service.usuario.DTO.UsuarioCriacaoDTO;
import gogood.authenticationJwt.service.usuario.UsuarioService;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioLoginDto;
import gogood.authenticationJwt.service.usuario.autenticacao.dto.UsuarioTokenDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> criar(@RequestBody @Valid UsuarioCriacaoDTO usuarioCriacaoDTO){
        this.usuarioService.criar(usuarioCriacaoDTO);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto){
        UsuarioTokenDto usuarioToken = this.usuarioService.autenticar(usuarioLoginDto);
        System.out.println(usuarioToken);
        return ResponseEntity.status(200).body(usuarioToken);
    }

    @GetMapping
    public ResponseEntity teste(){
        System.out.println("OI");
        return ResponseEntity.ok("Oi");
    }

}
