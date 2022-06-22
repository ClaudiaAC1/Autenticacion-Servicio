package proyecto.DSOS.Autenticacion.Service;

import java.util.List;
import java.util.Optional;

import proyecto.DSOS.Autenticacion.Model.Usuario;
/**
 *
 * @author claua
 * 
 */
public interface UsuarioService {
	public List getUsuario();
	
	public Optional<Usuario> getUser(long id);
	
	public Optional<Usuario> getUserByEmail(String email);
	
	public void updateUsuario(Usuario usuario, long id);

	public void deleteUsuario(long id);
}
