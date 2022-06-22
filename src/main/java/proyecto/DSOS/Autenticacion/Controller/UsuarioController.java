package proyecto.DSOS.Autenticacion.Controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyecto.DSOS.Autenticacion.Dto.ActualizarDto;

import proyecto.DSOS.Autenticacion.Dto.RegistroDTO;
import proyecto.DSOS.Autenticacion.Exception.BlogAppException;
import proyecto.DSOS.Autenticacion.Model.Rol;
import proyecto.DSOS.Autenticacion.Model.Usuario;
import proyecto.DSOS.Autenticacion.Repository.RolRepositorio;
import proyecto.DSOS.Autenticacion.Repository.UsuarioRepositorio;
import proyecto.DSOS.Autenticacion.Service.UsuarioService;
import proyecto.DSOS.Autenticacion.Utils.CustomResponse;

/**
 *
 * @author claua
 * 
 */

@RestController
@RequestMapping("/login/admin")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;    
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepositorio rolRepositorio;
 
    /**
     * Este metodo genera una lista de usuarios
     *@param 
     *@return formato json los usuarios registrados
     */
    @GetMapping("/")
    public CustomResponse getUsers() {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(usuarioService.getUsuario());
        //customResponse.setHttpCode(HttpStatus.OK.value());
        return customResponse;
    }

    /**
     * Este metodo permite obtener un usuarios por su correo electronico
     *@param email almacena el correo a buscar dentro de los datos de los usuarios
     *@return formato json al usuarios si se se encontro de lo contrario alguna leyenda del porque no..
     */
    @GetMapping("/{email}") //OBTENER usuario por email HTTPS:200
    public CustomResponse getUserByEmail(@PathVariable String email) {
        CustomResponse customResponse = new CustomResponse();
        customResponse.setData(usuarioService.getUserByEmail(email));
              
            if (customResponse.getData().hashCode() == 0) {
                throw new BlogAppException(HttpStatus.BAD_REQUEST, "Sin registro de ese correo");
              
            } else {
                throw new BlogAppException(HttpStatus.OK, "ok");
           
            }
    }

    /**
     * Este metodo permite editar un usuario por medio de su id
     *@param usuarioDTO alamena los datos del usuario a modificar 
     *@param id almacena el id del usuario a modificar
     *@return formato json si se actualizo correctamente o alguna leyenda del porque se no..
     */
    @PutMapping("/{id}") //EDITAR USUARIO HTTPS:201
    public CustomResponse updateUser(@RequestBody ActualizarDto usuarioDTO, @PathVariable Long id) {
        Usuario usuarioActual = usuarioRepositorio.getById(id); 
        Usuario usuario = new Usuario();
        Rol roles = rolRepositorio.findByNombre("ROLE_ADMIN").get();

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setEmail(usuarioActual.getEmail());
        usuario.setEstado(usuarioDTO.getEstado());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword())); //codifica contraseña		
        usuario.setRoles(Collections.singleton(roles));//Agrega rol ROLE_ADMIN)
        usuarioService.updateUsuario(usuario, id);
        throw new BlogAppException(HttpStatus.OK, "Usuario actualizado exitosamente");       

    }    
    //METODO INCORRECTO :(
//        @DeleteMapping("/{id}") // AUN NO QUEDA ESTE METODO :( HTTPS:???
//        public CustomResponse deleteUser(@PathVariable Long id) {
//                CustomResponse customResponse = new CustomResponse();
//                //borra primero en la foranea
//                Rol rol =  new Rol();
//                rol.setNombre("ROL_ADMIN");
//                rolRepositorio.save(rol);
//
//
//                usuarioService.deleteUsuario(id);
//                customResponse.setMesage("Usuario eliminado correctamente");
//
//                return customResponse;
//        }
}