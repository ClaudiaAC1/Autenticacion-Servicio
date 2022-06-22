package proyecto.DSOS.Autenticacion.ServiceImplements;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.DSOS.Autenticacion.Exception.ResourceNotFoundException;
import proyecto.DSOS.Autenticacion.Model.Usuario;
import proyecto.DSOS.Autenticacion.Repository.UsuarioRepositorio;
import proyecto.DSOS.Autenticacion.Service.UsuarioService;

@Service
public class UsuarioServiceImplements implements UsuarioService{

	@Autowired
	private UsuarioRepositorio usuarioRepository;
	
	@Override
	public List getUsuario() {		
		return usuarioRepository.findAll();
	}

	@Override
	public void updateUsuario(Usuario usuario, long id) {
		usuario.setId(id);
		usuarioRepository.save(usuario);
		
	}

	@Override
	public void deleteUsuario(long id) {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario ", "id", id));
		usuarioRepository.delete(usuario);
		
	}

	@Override
	public Optional<Usuario> getUser(long id) {
		
		return usuarioRepository.findById(id);
	}

	@Override
	public Optional<Usuario> getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByEmail(email);
	}

}
