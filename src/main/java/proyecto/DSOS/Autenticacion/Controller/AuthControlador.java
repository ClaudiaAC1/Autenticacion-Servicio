package proyecto.DSOS.Autenticacion.Controller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import proyecto.DSOS.Autenticacion.Dto.LoginDTO;
import proyecto.DSOS.Autenticacion.Dto.RegistroDTO;
import proyecto.DSOS.Autenticacion.Exception.BlogAppException;
import proyecto.DSOS.Autenticacion.Model.Rol;
import proyecto.DSOS.Autenticacion.Model.Usuario;
import proyecto.DSOS.Autenticacion.Repository.RolRepositorio;
import proyecto.DSOS.Autenticacion.Repository.UsuarioRepositorio;
import proyecto.DSOS.Autenticacion.Security.JwtTokenProvider;
import proyecto.DSOS.Autenticacion.Service.UsuarioService;
import proyecto.DSOS.Autenticacion.Utils.CustomResponse;
import proyecto.DSOS.Autenticacion.Utils.GeneradorPass;

/**
 *
 * @author claua
 *
 */
@RestController
@RequestMapping("/login")
public class AuthControlador {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepositorio usuarioRepository;

    @Autowired
    private RolRepositorio rolRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Este metodo permite validar las credenciales de incio de sesion (usuario
     * y contraseña)
     *
     * @param loginDTO que almacena las credenciales del usuario
     * @return formato json con el token de autenticacion si las credenciales
     * son correctas o alguna leyenda del porque no..
     */
    @PostMapping("/auth/user") //VALIDAR EL INICIO DE SESION DEL USUARIO 
    public CustomResponse authenticateUser(@RequestBody LoginDTO loginDTO) {
        CustomResponse customResponse = new CustomResponse();

        String usernameOrEmail = loginDTO.getUsernameOrEmail();
        Optional<Usuario> usuario = usuarioRepositorio.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (usuario.get().getEstado().equals("activo")) {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            //obtenemos el token del jwtTokenProvider
            String token = jwtTokenProvider.generarToken(authentication).getData().toString();
            customResponse.setData(token);
        } else {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "Usuario bloqueado");
        }
        return customResponse;

    }

    /**
     * Este metodo permite generar un nuevo usuario
     *
     * @param registroDTO que almacena los datos del nuevo usuario
     * @return formato json si se registro correctamente o alguna leyenda del
     * porque se no..
     */
    @PostMapping("/register")
    public CustomResponse registrarUsuario(@RequestBody RegistroDTO registroDTO) {

        if (usuarioRepositorio.existsByUsername(registroDTO.getUsername())) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST, "Ese nombre de usuario ya existe");

        }

        if (usuarioRepositorio.existsByEmail(registroDTO.getEmail())) {
            throw new BlogAppException(HttpStatus.BAD_REQUEST, "Ese email de usuario ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setUsername(registroDTO.getUsername());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setEstado(registroDTO.getEstado());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        Rol roles = rolRepositorio.findByNombre("ROLE_ADMIN").get();
        usuario.setRoles(Collections.singleton(roles));

        usuarioRepositorio.save(usuario);
        throw new BlogAppException(HttpStatus.CREATED, "Usuario registrado exitosamente");

    }

    /**
     * Este metodo permite validar el token que se genera al iniciar sesion
     *
     * @param token recibe el token
     * @return formato json con true si el token es correcto o alguna leyenda
     * del porque se rechazo.
     */
    @PostMapping("/auth/token/{token}")
    public CustomResponse validarTokendeInicioSesion(@PathVariable String token) {
        return jwtTokenProvider.validarToken2(token);

    }

    /**
     * Este metodo permite generar una contraseña aleatoria y la asigna al
     * usuario con el correo al que coincida.
     *
     *
     * @param email recibe un el correo electronico
     * @return formato json con la nueva contraseña generada.
     */
    @PostMapping("/recuperar/{email}")
    public CustomResponse generarContraseña(@PathVariable String email) {
        CustomResponse customResponse = new CustomResponse();
        GeneradorPass generador = new GeneradorPass();

        Optional<Usuario> usuario;

        Usuario aux = new Usuario(); //USUARIO QUE ACTUALIZARA EN LA BD 

        if (usuarioRepository.existsByEmail(email)) {
            usuario = usuarioService.getUserByEmail(email);
            String newPass = generador.generadorPass(12);
            System.out.println("contraseña nueva: " + newPass);

//la agregamos a la bd 
            Rol roles = rolRepositorio.findByNombre("ROLE_ADMIN").get();
            aux.setId(usuario.get().getId());
            aux.setNombre(usuario.get().getNombre());
            aux.setUsername(usuario.get().getUsername());
            aux.setEmail(usuario.get().getEmail());
            aux.setRoles(Collections.singleton(roles));//Agrega rol ROLE_ADMIN)
            aux.setPassword(passwordEncoder.encode(newPass));
            aux.setEstado(usuario.get().getEstado());

            usuarioService.updateUsuario(aux, aux.getId());

            customResponse.setData(newPass); //mandamos la nueva contraseña

        } else {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "El correo no coincide con algun usuario");
        }
        return customResponse;
    }

}
