package proyecto.DSOS.Autenticacion.ServiceImplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.DSOS.Autenticacion.Repository.RolRepositorio;
import proyecto.DSOS.Autenticacion.Service.RolService;

@Service
public class RolServiceImplements implements RolService{
	
	@Autowired
	private RolRepositorio rolRepository;
	
	@Override
	public void deleteRol_User(long id) {
		rolRepository.deleteById(id);		
	}

}
