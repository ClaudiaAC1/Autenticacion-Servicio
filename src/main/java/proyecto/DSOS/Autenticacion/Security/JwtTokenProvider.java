package proyecto.DSOS.Autenticacion.Security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import proyecto.DSOS.Autenticacion.Exception.BlogAppException;
import proyecto.DSOS.Autenticacion.Utils.CustomResponse;

/**
 *
 * @author claua
 * 
 */

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

     /**
     * Metodo que generar el token 
     *
     * @param authentication
     * @return  customResponse
     */  
    public CustomResponse generarToken(Authentication authentication) { // GENERA TOKEN
        CustomResponse customResponse = new CustomResponse();
        String username = authentication.getName();
        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(fechaActual.getTime() + jwtExpirationInMs);

        String token = Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(fechaExpiracion)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
        
        customResponse.setData(token);
        customResponse.setHttpCode(HttpStatus.ACCEPTED.value());
        return customResponse;
    }

    public String obtenerUsernameDelJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    
     /**
     * Metodo que validar el token 
     *
     * @param authentication
     * @return  boolean
     */  
    public boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "Firma JWT no valida");
        } catch (MalformedJwtException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "Token JWT no valida");
        } catch (ExpiredJwtException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "Token JWT caducado");
        } catch (UnsupportedJwtException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "Token JWT no compatible");
        } catch (IllegalArgumentException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "La cadena claims JWT esta vacia");
        }
    }

    /**
     * Metodo que validar el token 
     *
     * @param authentication
     * @return  customResponse
     */
    public CustomResponse validarToken2(String token) {
        CustomResponse customResponse = new CustomResponse();

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            
            customResponse.setHttpCode(HttpStatus.OK.value()); // o HTTPS: 100 O 200
            customResponse.setData(true);
           
        } catch (SignatureException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "Firma JWT no valida");
            
        } catch (MalformedJwtException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "Firma JWT no valida");

        } catch (ExpiredJwtException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "Token JWT caducado");

        } catch (UnsupportedJwtException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "Token JWT caducado");
			
        } catch (IllegalArgumentException ex) {
            throw new BlogAppException(HttpStatus.UNAUTHORIZED, "La cadena claims JWT esta vacia");

        }

       return customResponse;
    }

}
