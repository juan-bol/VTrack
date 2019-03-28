package co.com.tracert.vtrack.web.named;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class HeaderInAppNB extends NamedBeanUtils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2619513910071463998L;

	/**
	 * Hace referencia al usuario que se logueó a la app
	 */
	private Usuario usuSesion;
	
	/**
	 * Hace referencia al usuario impersonate seleccionado en el select one menu
	 */
	private Usuario usuImp;

	/**
	 * Delegado que se conectara con la lógica
	 */
	@EJB
	private DelegadoEJB delegado;
	
	/**
	 * Es la lista de usuarios a los cuales el usuario en sesión tiene 
	 * permiso tanto para ver como editar su información
	 */
	private List<Permiso> listPermisos;
	
	/**
	 * Hace referencia al id del usuario seleccionado en el select one menu
	 */
	private Long idUsuSeleccionado;
	
	/**
	 * Aquí se va a guarda el permiso que se selecciona en el select one menu
	 */
	private Permiso permisoSeleccionado;
	
	public HeaderInAppNB() {
		
	}

	@PostConstruct
	public void init() {
		usuSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
		usuImp = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
		//Se debe ingresar a la lista de permisos el permiso correspondiente al usuario logueado
		Permiso permisoUsuario = new Permiso();
		permisoUsuario.setNombreCuenta("Su información");
		permisoUsuario.setUsuarioDestino(usuSesion);
		//No debe existir ningun usuario con id 0L
		idUsuSeleccionado = 0L;
		//Si el permiso seleccionado no está en sesión significa que no ha sido seleccionado nada del select one menu
		permisoSeleccionado = (Permiso) getFromSession(ConstantesVtrack.PERMISO_SELECCIONADO);
		if(permisoSeleccionado == null) {
			//Se le asigna el permiso por defecto, es el correspondiente con el usuario logueado
			permisoSeleccionado = permisoUsuario;
		}
		try {
			//Se llena con la lista de permisos asociados a ese usuario en Sesión 
			listPermisos = delegado.darPermisosDestinoDeAprobado(usuSesion);
			if(permisoSeleccionado.getUsuarioDestino().getIdUsuario() != usuSesion.getIdUsuario()) {
				//Se añade el permiso del usuario logueado, para que se pueda seleccionar en el select one menu
				//cuando no hubo nada seleccionado en el select one menu
				listPermisos.add(permisoUsuario);
				//boolean b = listPermisos.remove(permisoSeleccionado);
				borrarPermisoRepetido(permisoSeleccionado.getUsuarioDestino().getIdUsuario());
			}
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Cuando cambia el valor del select one menu cambia el usuario impersonate con el fin
	 * 				de que las todas páginas muestren la información que corresponde a este usuario.
	 * @fecha 1/11/2018
	 */
	public void cambiarUsuarioImpersonate() {
		//Si selecciona el usuario con el id del usuario logueado (en sesion)
		if(idUsuSeleccionado == usuSesion.getIdUsuario()) {
			//El usuario Impersonate es null
			usuImp = null;
			putInSession(ConstantesVtrack.USUARIO_IMP, null);
			permisoSeleccionado = obtenerPermisoSeleccionado(idUsuSeleccionado);
			//cuando se selecciona algo en el select one menu debe subirlo a la sesión
			putInSession(ConstantesVtrack.PERMISO_SELECCIONADO, permisoSeleccionado);
			addMessage(FacesMessage.SEVERITY_INFO, "Se mostrará información de su perfil");
		}
		
		//Cuando selecciona a un usuario específico
		else{
			usuImp = obtenerPermisoSeleccionado(idUsuSeleccionado).getUsuarioDestino();
			putInSession(ConstantesVtrack.USUARIO_IMP, usuImp);
			permisoSeleccionado = obtenerPermisoSeleccionado(idUsuSeleccionado);
			//cuando se selecciona algo en el select one menu debe subirlo a la sesión
			putInSession(ConstantesVtrack.PERMISO_SELECCIONADO, permisoSeleccionado);
			
			if(permisoSeleccionado.getTipoPermiso().equals(ConstantesVtrack.PERMISO_ASOCIADO)) {
				addMessage(FacesMessage.SEVERITY_INFO, "Se mostrará información del perfil de: " + permisoSeleccionado.getUsuarioDestino().getNombre() + " " + permisoSeleccionado.getNombreCuenta());
			}
			else {
				addMessage(FacesMessage.SEVERITY_INFO, "Se mostrará información del perfil de: " + permisoSeleccionado.getNombreCuenta());
			}
			
			
		}
	}
	
	/**
	 * @author Steven M
	 * @descrpcion Busca el permiso que se selecciono en la lista de select one menu a partir de su id
	 * @param id del usuario que se busca en la lista del NB permisos
	 * @return
	 */
	public Permiso obtenerPermisoSeleccionado(Long id) {
		Permiso permiso = null;
		boolean encontro = false;
		for (int i = 0; i < listPermisos.size() && !encontro; i++) {
			if(listPermisos.get(i).getUsuarioDestino().getIdUsuario() == id) {
				permiso = listPermisos.get(i);
				encontro = true;
			}
		}		
		return permiso;
	}
	
	/**
	 * @author Steven M
	 * @descripcion Borra de la lista de permisos el usuario al que le corresponda ese id.
	 * Evita la repetición en la lista de permisos del select one menu
	 * @param id del usuario al que se quiere borra de las lista de permisos
	 * @fecha 4/11/2018
	 */
	public void borrarPermisoRepetido(Long id) {
		boolean encontro = false;
		for (int i = 0; i < listPermisos.size() && !encontro; i++) {
			if(listPermisos.get(i).getUsuarioDestino().getIdUsuario() == id) {
				listPermisos.remove(i);
				encontro = true;
			}
		}	
	}
	
	public void actualizarPagina() {
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formHeader:menuPermisos");
//		 RequestContext requestContext = RequestContext.getCurrentInstance();
//		 requestContext.update("formHeader");
		
	}
	
	public List<Permiso> getListPermisos() {
		return listPermisos;
	}

	public void setListPermisos(List<Permiso> listPermisos) {
		this.listPermisos = listPermisos;
	}

	public Usuario getUsuSesion() {
		return usuSesion;
	}

	public void setUsuSesion(Usuario usuSesion) {
		this.usuSesion = usuSesion;
	}

	public Long getIdUsuSeleccionado() {
		return idUsuSeleccionado;
	}

	public void setIdUsuSeleccionado(Long idUsuSeleccionado) {
		this.idUsuSeleccionado = idUsuSeleccionado;
	}

	public Usuario getUsuImp() {
		return usuImp;
	}

	public void setUsuImp(Usuario usuImp) {
		this.usuImp = usuImp;
	}

	public Permiso getPermisoSeleccionado() {
		return permisoSeleccionado;
	}

	public void setPermisoSeleccionado(Permiso permisoSeleccionado) {
		this.permisoSeleccionado = permisoSeleccionado;
	}
	
}
