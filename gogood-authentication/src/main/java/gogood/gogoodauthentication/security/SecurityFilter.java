package gogood.gogoodauthentication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        System.out.println("Token JWT recebido: " + tokenJWT); // Adiciona para verificar se o token é recuperado corretamente

        if (tokenJWT != null){
            System.out.println("Validando token..."); // Adiciona para verificar se a validação do token é chamada
            if (tokenService.validarToken(tokenJWT)) {
                var subject = tokenService.getSubject(tokenJWT);
                // Se desejar, você pode configurar a autenticação do usuário aqui
                // Por exemplo, pode ser útil configurar o SecurityContext com o usuário autenticado
                // SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Se o token não for válido, você pode lidar com isso de acordo com sua lógica de negócio
                // Por exemplo, pode retornar uma resposta de erro 401 (Não autorizado)
                System.out.println("Token inválido"); // Adiciona para verificar se o token é inválido
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            System.out.println("Token JWT não encontrado na requisição"); // Adiciona para verificar se o token não é encontrado
        }

        filterChain.doFilter(request,response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
