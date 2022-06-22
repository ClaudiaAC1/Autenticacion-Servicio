package proyecto.DSOS.Autenticacion.Security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author claua
 *@ Clase que maneja errore cuando no se esta autenticado
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//CLASE PARA MANEJAR ERRORES CUANOD NO ESTA AUTENTICADO

    /**
     *
     * @param request
     * @param response
     * @return User
     *
     * @see IOException
     * @see ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

}
