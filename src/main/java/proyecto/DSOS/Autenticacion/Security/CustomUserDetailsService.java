package proyecto.DSOS.Autenticacion.Security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import proyecto.DSOS.Autenticacion.Model.Rol;
import proyecto.DSOS.Autenticacion.Model.Usuario;
import proyecto.DSOS.Autenticacion.Repository.UsuarioRepositorio;
import proyecto.DSOS.Autenticacion.Utils.CustomResponse;

/**
 *
 * @author claua
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    /**
     * Este metodo permite buscar y cargar la sesion del usuario
     *
     * @param usernameOrEmail
     * @return User
     * 
     * @see UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        CustomResponse customResponse = new CustomResponse();
        customResponse.setHttpCode(HttpStatus.NOT_FOUND.value());
        customResponse.setMensage("Usuario no encontrado con ese username o email : " + usernameOrEmail);

        Usuario usuario = usuarioRepositorio.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                "Usuario no encontrado con ese username o email : "
                + usernameOrEmail));

        return new User(usuario.getEmail(), usuario.getPassword(), mapearRoles(usuario.getRoles()));
    }

     /**
     * Este metodo mapea los roles que existen
     *
     * @param roles
     * @return User
     * 
     * @see Usuario
     */
    private Collection<? extends GrantedAuthority> mapearRoles(Set<Rol> roles) {
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getNombre())).collect(Collectors.toList());
    }
}
