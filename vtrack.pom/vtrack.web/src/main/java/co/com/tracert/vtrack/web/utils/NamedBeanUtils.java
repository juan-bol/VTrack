package co.com.tracert.vtrack.web.utils;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;


public class NamedBeanUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5345371790330082841L;
	
	
	/**
	 * Obtiene un objeto usuario de la sesión a partir de la llave ingresada
	 * @param key
	 * @Autor David
	 * @return
	 */
	public Object getFromSession(String key) {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		return fc.getExternalContext().getSessionMap().get(key);
	}
	
	
	/**
	 * Sube un objeto usuario a la sesión 
	 * @param key
	 * @Autor David
	 * @return
	 */
	public void putInSession(String key, Object value) {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		fc.getExternalContext().getSessionMap().put(key, value);
	}
	
	/**
	 * Obtiene un objeto de la sesión de aplicación (visible para todas las sesiones)
	 * @param key
	 * @Autor David
	 * @return
	 */
	public Object getFromApplication(String key) {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		return fc.getExternalContext().getApplicationMap().get(key);
	}
	
	/**
	 * Sube un objeto a la sesión de aplicación (visible para todas las sesiones)
	 * @param key
	 * @Autor David
	 * @return
	 */
	public void putInApplication(String key, Object value) {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		fc.getExternalContext().getApplicationMap().put(key, value);
	}
	
	/**
	 * Muestra un mensaje en la interfaz principal con la descripción y nivel de
	 * gravedad pasada por parametro
	 * 
	 * @param nivelGravedad es una de las siguientes constantes:
	 *                      FacesMessage.SEVERITY_INFO, FacesMessage.SEVERITY_ERROR,
	 *                      FacesMessage.SEVERITY_FATAL, FacesMessage.SEVERITY_WARN
	 * @param descripcion
	 */
	public void addMessage(Severity nivelGravedad, String descripcion) {
		FacesMessage message = new FacesMessage(nivelGravedad, descripcion, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
