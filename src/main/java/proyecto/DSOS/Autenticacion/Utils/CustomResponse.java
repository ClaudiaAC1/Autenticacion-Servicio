package proyecto.DSOS.Autenticacion.Utils;

import java.util.LinkedList;

/**
 *
 * @author claua
 * 
 */
public class CustomResponse {
	private Integer httpCode;
	private Object data;
	private String mensage;

	public CustomResponse() {
		this.httpCode = 200;
		data = new LinkedList();
		this.mensage = "Ok";
	}
        
	public Integer getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMensage() {
		return mensage;
	}

	public void setMensage(String mensage) {
		this.mensage = mensage;
	}
}
