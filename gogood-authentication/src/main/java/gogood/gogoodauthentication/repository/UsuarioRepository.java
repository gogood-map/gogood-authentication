package gogood.gogoodauthentication.repository;

import gogood.gogoodauthentication.DTO.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByLogin(String login);


    boolean existsByToken(String entrada);


    Usuario findByToken(String entrada);

    UserDetails findByLogin(String login);
}
  

    
