package gogood.gogoodauthentication.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import gogood.gogoodauthentication.DTO.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    private static final String SECRET = "suaChaveSecreta";
    private static final String ISSUER = "gogood-authentication";

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getLogin() + usuario.getSenha() + usuario.getToken())
                    .withExpiresAt(Date.from(dataExpiracao()))
                    .sign(algorithm);
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token Inválido", exception);
        }
    }

    private Instant dataExpiracao() {
        return Date.from(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC)).toInstant();
    }

    public boolean validarToken(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT);
            return true; // Se não lançar exceção, o token é válido
        } catch (JWTVerificationException e) {
            return false; // Se lançar exceção, o token é inválido
        }
    }
}
