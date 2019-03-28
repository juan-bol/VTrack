package co.com.tracert.vtrack.web.named;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;
import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Estado;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;

@ViewScoped
@Named
public class PermisosRecibidosNB extends NamedBeanUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4786863664049011579L;

	@EJB
	private DelegadoEJB delegado;

	/**
	 * Con este atributo se asegura que se muestre o no información en la pag de
	 * compartir permisos false = no se muestra las dif opciones a usuarios
	 * impersonate true = se muestre las dif opciones a un usuario logueado
	 */
	private boolean usuLogueado;

	private Usuario usuarioSesion;

	private List<Permiso> listPermisosRecibidos;

	/**
	 * Hace referencia al permiso que se selecciona en la vista
	 */
	private Permiso permisoModificarTmp;

	/**
	 * Hace referencia al nombre de la cuenta escrito en el input text del dialogo
	 * "parentescoDialogo"
	 */
	private String nombreCuentaTmp;


	@PostConstruct
	private void init() {

		asignarUsuarioSesion();

		try {
			// Lista de los permisos ha que recibido el usuario que entra como parametro
			listPermisosRecibidos = delegado.darPermisosDestinoDe(usuarioSesion);
			// Ordena la lista de permisos
			ordenarListaPermisos();
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener los permisos recibidos " + e.getMessage());
		}

	}

	/**
	 * @author Steven M
	 * @descripcion asigna a la var usuarioSesion el valor dependiendo de si hay o
	 *              no usuario Impersonate : 1. Correspondiente con el usuario que
	 *              se logeo 2. Correspondiente con el usuario impersonate
	 * @fecha 1/11/2018
	 */
	public void asignarUsuarioSesion() {
		Usuario usuarioImp = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
		// Si hay usuario impersonate entonces el usuario en sesión es el usuario
		// impersonate
		if (usuarioImp != null) {
			usuarioSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
			usuLogueado = false;

			// En caso de que no exista usuario impersonate entonces el usuario en sesión es
			// el usuario logeado
		} else {
			usuarioSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
			usuLogueado = true;
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Al permiso seleccionado en la vista se le asigna el estado
	 *              Ignorado y se envia a la lógica para que guarde su estado.
	 * @fecha 2/12/2018
	 */
	public void ignorarPermiso() {
		try {

			// Al permiso selecionado en la vista se le asigna el estado Ignorado
			Estado estadoTmp = new Estado();
			estadoTmp.setEstado(ConstantesVtrack.ESTADO_IGNORADO);
			permisoModificarTmp.setEstado(estadoTmp);

			// Se envia a la lógica el permiso modificado
			delegado.configurarPermisoGeneral(permisoModificarTmp);
			addMessage(FacesMessage.SEVERITY_INFO, "Se ha ignorado el permiso exitosamente");
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, "Error al ignorar el permiso " + e.getMessage());
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Se le agrega al permiso seleccionado en la vista el nombre de la
	 *              cuenta escrito en el dialogo desplegado, y se le asigna el
	 *              estado APROBADO a dicho permiso.
	 * @fecha 2/12/2018
	 */
	public void agregarNombreCuentaYAprobar() {

		try {

			// Se asegura de que ingrese un nombre de cuenta no vacio
			if (nombreCuentaTmp == null || nombreCuentaTmp.equals("") || nombreCuentaTmp.trim().equals("")) {
				throw new Exception("Debe ingresar un nombre de cuenta");
			}

			// Se le asigna el nombre de la cuenta escrito en el input text en el dialogo
			permisoModificarTmp.setNombreCuenta(nombreCuentaTmp);
			// Se le asigna un estado aprobado al permiso
			Estado estadoTmp = new Estado();
			estadoTmp.setEstado(ConstantesVtrack.ESTADO_APROBADO);
			permisoModificarTmp.setEstado(estadoTmp);

			addMessage(FacesMessage.SEVERITY_INFO, "Se ha agregado el nombre de la cuenta exitosamente");

			// Se envia a la lógica el permiso modificado
			delegado.configurarPermisoGeneral(permisoModificarTmp);
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, "Error al agregar nombre de cuenta a un permiso " + e.getMessage());
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Método que cierra el diálogo de ingresar nombre de parentesco
	 * @fecha 2/12/2018
	 */
	public void cerrarDialogoParentesco() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('parentescoDialogo').hide()");
	}

	/**
	 * @author Steven M
	 * @descripcion Ordena la lista de permisos recibidos de acuerdo al correo de
	 *              usuario destino de cada permiso
	 * @fecha 5/12/2018
	 */
	public void ordenarListaPermisos() {
		Collections.sort(this.listPermisosRecibidos, (a, b) ->  a.getUsuarioDestino().getCuenta().getCorreo()
				.compareToIgnoreCase(b.getUsuarioDestino().getCuenta().getCorreo()));
	}

	public boolean isUsuLogueado() {
		return usuLogueado;
	}

	public void setUsuLogueado(boolean usuLogueado) {
		this.usuLogueado = usuLogueado;
	}

	public List<Permiso> getListPermisosRecibidos() {
		return listPermisosRecibidos;
	}

	public void setListPermisosRecibidos(List<Permiso> listPermisosRecibidos) {
		this.listPermisosRecibidos = listPermisosRecibidos;
	}

	public Permiso getPermisoModificarTmp() {
		return permisoModificarTmp;
	}

	public void setPermisoModificarTmp(Permiso permisoModificarTmp) {
		this.permisoModificarTmp = permisoModificarTmp;
	}

	public String getNombreCuentaTmp() {
		return nombreCuentaTmp;
	}

	public void setNombreCuentaTmp(String nombreCuentaTmp) {
		this.nombreCuentaTmp = nombreCuentaTmp;
	}

}
