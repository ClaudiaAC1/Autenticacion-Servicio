package proyecto.DSOS.Autenticacion.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import proyecto.DSOS.Autenticacion.Model.Rol;

/**
 *
 * @author claua
 *
 */
public interface RolRepositorio extends JpaRepository<Rol, Long> {

    /**
     * Este metodo permite buscar el rol por su nombre
     *
     * @param nombre 
     * @return 
     */
    public Optional<Rol> findByNombre(String nombre);

}
