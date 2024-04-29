package gogood.gogoodauthentication.repository;

import gogood.gogoodauthentication.DTO.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByEmail(String email);

    Usuario findByEmail(String email);

    boolean existsByToken(String entrada);


    Usuario findByToken(String entrada);
}
  

    
