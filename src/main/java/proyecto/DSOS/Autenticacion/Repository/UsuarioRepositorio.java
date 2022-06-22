package proyecto.DSOS.Autenticacion.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import proyecto.DSOS.Autenticacion.Model.Usuario;

/**
 *
 * @author claua
 *
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    /**
     * Este metodo permite buscar el rol por su correo
     *
     * @param email 
     * @return Usuario
     */
    public Optional<Usuario> findByEmail(String email);
    
    /**
     * Este metodo permite buscar el usuarios por su usuario o correo
     *
     * @param username
     * @param email 
     * @return Usuario
     */
    public Optional<Usuario> findByUsernameOrEmail(String username, String email);

    
    /**
     * Este metodo permite buscar el usuario por su nombre de usuario
     *
     * @param username 
     * @return Usuario
     */
    public Optional<Usuario> findByUsername(String username);
    
    
    /**
     * Este metodo permite buscar si existe existe o no el nombre de usuario
     *
     * @param username 
     * @return boolean
     */
    public Boolean existsByUsername(String username);

    /**
     * Este metodo permite buscar si existe existe o no el correo
     *
     * @param email que almacena las credenciales del usuario
     * @return 
     */
    public Boolean existsByEmail(String email);

    //public Usuario findByEmaill(String email);
}
