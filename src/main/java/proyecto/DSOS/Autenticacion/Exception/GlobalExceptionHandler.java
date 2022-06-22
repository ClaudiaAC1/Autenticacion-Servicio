package proyecto.DSOS.Autenticacion.Exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import proyecto.DSOS.Autenticacion.Utils.CustomResponse;

/**
 *
 * @author claua
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponse> manejarResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        CustomResponse errorDetalles = new CustomResponse();
        errorDetalles.setHttpCode(HttpStatus.NOT_FOUND.value());
        errorDetalles.setMensage("Solicitud denegada NOT FOUND");
        return new ResponseEntity<>(errorDetalles, HttpStatus.NOT_FOUND);
    }

    /**
     * Manejador de excepciones genarada con la clase BlogAppEception
     *
     * @see BlogAppException
     *
     * @param exception
     * @param webRequest
     * @return los detalles de la excepcion
     */
    @ExceptionHandler(BlogAppException.class)
    public ResponseEntity<CustomResponse> manejarBlogAppException(BlogAppException exception, WebRequest webRequest) {

        CustomResponse errorDetalles = new CustomResponse();
        //(exception.getMessage(), webRequest.getDescription(false));
        errorDetalles.setHttpCode(exception.getEstado().value());
        errorDetalles.setMensage(exception.getMensaje());
        return new ResponseEntity<>(errorDetalles, exception.getEstado());
    }

    /**
     * Manejador de excepciones genarada cuando no se rellenan los campos correctamente
     *
     * @see DataIntegrityViolationException
     *
     * @param exception
     * @param webRequest
     * @return los detalles de la excepcion
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomResponse> manejarExceptionVar(Exception exception, WebRequest webRequest) {
        System.out.println("entro1");
        CustomResponse errorDetalles = new CustomResponse();
        errorDetalles.setHttpCode(HttpStatus.BAD_REQUEST.value());
        errorDetalles.setMensage("Rellenar todos los campo");
        //errorDetalles.setMensage(exception.getMessage());
        return new ResponseEntity<>(errorDetalles, HttpStatus.BAD_REQUEST);
    }
    
    
    /**
     * Manejador de excepciones generales
     *
     * @see Exception
     *
     * @param exception
     * @param webRequest
     * @return los detalles de la excepcion
     */
    @ExceptionHandler(Exception.class) //EXCEPCION PARA CUANDo NO ENCUENTRA EL USUARIO o son credenciales incorrectas
    public ResponseEntity<CustomResponse> manejarGlobalException(Exception exception, WebRequest webRequest) {

        CustomResponse errorDetalles = new CustomResponse();
        errorDetalles.setHttpCode(HttpStatus.UNAUTHORIZED.value());
        //errorDetalles.setData("Credenciales incorrectas");
        errorDetalles.setMensage("Acceso denegado");
        return new ResponseEntity<>(errorDetalles, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Manejador de excepciones 
     *EXCEPCION QUE SE LANZA CUANDO ALGUN CAMPO ES RELLENADO INCORRECTAMENTE
     * @param exception
     * @param webRequest
     * @return los detalles de la excepcion
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();

            errores.put(nombreCampo, mensaje);
        });

        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

}
