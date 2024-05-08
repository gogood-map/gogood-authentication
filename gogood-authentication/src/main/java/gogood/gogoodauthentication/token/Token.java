package gogood.gogoodauthentication.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import gogood.gogoodauthentication.DTO.Usuario;
import org.springframework.stereotype.Service;

@Service
public class Token {
    String token;
    private static String tokenSecretKey = "vnhscincijdafiocjmfj-w=fiahavobksdjvfjhsdfdgahgapjhofjgopjopbpjgofgj";

    public String getToken(Usuario usuario) {
        long exp = System.currentTimeMillis() + (60 * 60 * 100);

        token = JWT.create()
                .withClaim("id",usuario.getId().toString())
                .withClaim("username",usuario.getNome())
                .withClaim("password",usuario.getPassword())
                .withClaim("email",usuario.getLogin())
                .sign(Algorithm.HMAC256(tokenSecretKey));

        return token;
    }
    public String getUserIdByToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(tokenSecretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getClaims().get("id").asString();
    }
}
