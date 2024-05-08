package gogood.gogoodauthentication.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final AntPathRequestMatcher[] URL_PERMITIDAS = {
            new AntPathRequestMatcher("/usuarios/login/**"),
            new AntPathRequestMatcher("/usuarios/cadastrar/**"),
            new AntPathRequestMatcher("/usuarios")
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf().disable()
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(URL_PERMITIDAS).permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
