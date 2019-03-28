package co.com.tracert.vtrack.web.named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Estado;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class CompartirCarneNB extends NamedBeanUtils implements Serializable {

	private static final long serialVersionUID = 8827473436388155001L;

	@EJB
	private DelegadoEJB delegado;

	/**
	 * Con este atributo se asegura que se muestre o no información en la pag de
	 * compartir permisos false = no se muestra las dif opciones a usuarios
	 * impersonate true = se muestre las dif opciones a un usuario logueado
	 */
	private boolean usuLogueado;

	/**
	 * Hace referencia al usuario seleccionado en el header Puede ser uno
	 * impersonate o el usuario que se logueo
	 */
	private Usuario usuarioSesion;

	/**
	 * El tipo de permiso que se le va a conceder a un usuario Puede ser de lectura
	 * o lectura y escritura
	 */
	private String tipoPermisoSeleccionadoConceder;

	/**
	 * Hace referencia al permiso tipo lectura
	 */
	private String tipoLectura;

	/**
	 * Hace referencia al permiso tipo lectura y escritura
	 */
	private String tipoEscritura;

	/**
	 * Hace referencia al correo escrito en el inputText
	 */
	private String correoIngresado;

	/**
	 * La lista de permisos que el usuario ha concedido
	 */
	private List<Permiso> listaPermisos;

	/**
	 * Hace referencia al permiso a borrar que se selecciona en la vista
	 * compartirCarne.xhtml
	 */
	private Permiso permisoABorrar;

	/**
	 * Hace referencia a los permisos que les han cambiado el tipo de permiso en la
	 * vista y deben se persistidos en la DB
	 */
	private List<Permiso> listaPermisosACambiar;

	/**
	 * La lista de permisos que el usuario ha concedido, auxiliar debido a que se
	 * necesita para comprobar si los permisos han sufrido o no modificaciones
	 */
	private List<Permiso> listaPermisosAuxiliar;

	public CompartirCarneNB() {

	}

	@PostConstruct
	private void init() {

		asignarUsuarioSesion();

		correoIngresado = "";
		tipoLectura = ConstantesVtrack.PERMISO_LECTURA;
		tipoEscritura = ConstantesVtrack.PERMISO_ESCRITURA;

		listaPermisosACambiar = new ArrayList<Permiso>();

		try {
			// Lista de los permisos ha que concedio el usuario que entra como parametro
			listaPermisos = delegado.darPermisosOrigenDe(usuarioSesion);
			listaPermisosAuxiliar = delegado.darPermisosOrigenDe(usuarioSesion);
			// Ordena la lista de permisos
			ordenarListaPermisos();
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener los permisos concedidos " + e.getMessage());
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
	 * @descripcion Se crea y concede un nuevo permiso al usuario con el correo
	 *              escrito en el input text y con el tipo de permiso escogido en el
	 *              select one menu
	 * @fecha 1/12/2018
	 */
	public void concederPermisoAPorCorreo() {
		// Se crea el nuevo permiso que va a ser mandado a la lógica
		Permiso permisoNuevo = new Permiso();
		// Se asigna el tipo de Lectura o Escritura segun se haya seleccionado en el
		// select one menu
		permisoNuevo.setTipoPermiso(tipoPermisoSeleccionadoConceder);
		// Usuario destino es el de la sesión logueado, es quien concede el permiso
		permisoNuevo.setUsuarioDestino(usuarioSesion);
		Estado estadoNuevo = new Estado();
		// Inicialmente del estado del permiso es en espera
		estadoNuevo.setEstado(ConstantesVtrack.ESTADO_EN_ESPERA);
		permisoNuevo.setEstado(estadoNuevo);
		
		try {
			delegado.concederPermisoCorreo(permisoNuevo, correoIngresado);
			addMessage(FacesMessage.SEVERITY_INFO, "Se ha concedido el permiso exitosamente");
			PrimeFaces.current().executeScript("setTimeout(actualizarPagina, 1100)");
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error al conceder permiso con correo: " + e.getMessage());
		}
	}

	/**
	 * @author Steven M
	 * @descripcion verifica que el permiso pasado por parametro no se encuentre en
	 *              la lista de permisos a persistir. Además, agrega el permiso
	 *              pasado por parametro si no está en la lista de permisos
	 *              concedidos con igual tipo de permiso, es decir, lo agrega si ha
	 *              sufrido un cambio en el tipo de permiso.
	 * @param permiso que se va a agregar a la lista de permisos a persistir
	 * @fecha 10/12/2018
	 */
	public void cambiarTipoPermisoConcedido(Permiso permiso) {

		int indiceConCambios = buscarPermisoConCambios(permiso);

		// Si el permiso al cual se le cambió el tipo de permiso ya está en la lista
		// de permisos a cambiar, se borra de esa lista.
		// Si no esta en la lista, pues no se borra
		if (indiceConCambios >= 0) {
			listaPermisosACambiar.remove(indiceConCambios);
		}

		// Se agrega a la lista de permisos que van a ser persitidos, si y solo se
		// produjo
		// un cambio en el tipo de permiso, es decir, si cambió el tipo de permiso
		if (!buscarPermisoSinCambios(permiso)) {
			// Agrega el permiso a la lista de permisos que van a ser persistidos
			listaPermisosACambiar.add(permiso);
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Guarda en la DB los cambios de los permisos dentro de la lista
	 *              de permisos a cambiar.
	 * @fecha 10/12/2018
	 */
	public void guardarCambios() {

		if (listaPermisosACambiar.isEmpty()) {
			addMessage(FacesMessage.SEVERITY_WARN, "Debe realizar un cambio");
		} else {
			try {
				Permiso permisoTmp;
				// Cada uno de los permisos en la lista deben ser persistidos
				for (int i = 0; i < listaPermisosACambiar.size(); i++) {

					permisoTmp = listaPermisosACambiar.get(i);
					delegado.configurarPermisoGeneral(permisoTmp);
				}

				if (listaPermisosACambiar.size() > 1) {
					addMessage(FacesMessage.SEVERITY_INFO,
							"Se han guardado " + listaPermisosACambiar.size() + " cambios exitosamente");
				} else {
					addMessage(FacesMessage.SEVERITY_INFO, "Se ha guardado un cambio exitosamente");
				}

			} catch (Exception e) {
				addMessage(FacesMessage.SEVERITY_ERROR, "Error modificando el tipo permiso: " + e.getMessage());
			}
		}
		
		// La lista de permisos a cambiar vuelve a estar vacia
		listaPermisosACambiar = new ArrayList<Permiso>();
		//La lista de permisos concedidos auxiliar debe actualizarse
		try {
			// Lista de los permisos ha que concedio el usuario que entra como parametro
			listaPermisosAuxiliar = delegado.darPermisosOrigenDe(usuarioSesion);
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener los permisos concedidos auxiliar" + e.getMessage());
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Busca en la lista de permisos a cambiar si el permiso pasado
	 *              pasado por parametro se encuentra en ella. En caso afirmativo
	 *              retorna el indice. En caso negativo retorna -1;
	 * @param permiso que se va a buscar en la lista.
	 * @fecha 10/12/2018
	 * @return el indice del permiso pasado por parametro en la lista de permisos a
	 *         cambiar
	 */
	public int buscarPermisoConCambios(Permiso permiso) {
		int indicePermiso = -1;
		boolean encontro = false;
		Permiso permisoTmp;
		for (int i = 0; i < listaPermisosACambiar.size() && !encontro; i++) {

			permisoTmp = listaPermisosACambiar.get(i);

			if (permisoTmp.getIdPermiso() == permiso.getIdPermiso()) {
				indicePermiso = i;
				encontro = true;
			}
		}
		return indicePermiso;
	}

	/**
	 * @author Steven M
	 * @descripcion busca en la lista de permisos concedidos auxilair si el permiso pasado
	 *              por parametro se encuentra en ella y verifica que no se han
	 *              producido cambios en cuanto al tipo de permiso
	 * @param permiso que se buscará en la lista de permisos concedidos auxiliar para
	 *                verificar si ha sufrido cambios
	 * @fecha 10/12/2018
	 * @return true o false si encontró o no el permiso en la lista de permisos sin
	 *         cambios en el tipo de permiso
	 */
	public boolean buscarPermisoSinCambios(Permiso permiso) {

		boolean encontro = false;
		Permiso permisoTmp;
		for (int i = 0; i < listaPermisosAuxiliar.size() && !encontro; i++) {

			permisoTmp = listaPermisosAuxiliar.get(i);

			if (permisoTmp.getIdPermiso() == permiso.getIdPermiso()
					&& permisoTmp.getTipoPermiso().equals(permiso.getTipoPermiso())) {
				encontro = true;
			}

		}

		return encontro;
	}

	/**
	 * @author Steven M
	 * @descripcion Llama al delegado para que modifique el permiso en la DB con su
	 *              nuevo tipo de estado (inactivo). Al presionar el botón borrar se
	 *              "setea" el permiso tmp del xhtml en la variable del NB llamada
	 *              permisoABorrar
	 * @fecha 1/12/2018
	 */
	public void eliminarPermisoConcedido() {
		try {
			// Crea un estado con el tipo estado INACTIVO
			Estado nuevoEstado = new Estado();
			nuevoEstado.setEstado(ConstantesVtrack.ESTADO_INACTIVO);
			permisoABorrar.setEstado(nuevoEstado);

			// Llama al método que modifica el permiso
			delegado.configurarPermisoGeneral(permisoABorrar);

			addMessage(FacesMessage.SEVERITY_INFO, "Se ha eliminado el permiso exitosamente");

		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error eliminando el tipo permiso: " + e.getMessage());
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Método que cierra el diálogo de confirmar borrar permiso
	 * @fecha 2/12/2018
	 */
	public void cerrarConfirmacion() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('confirmarDialogo').hide()");
	}

	/**
	 * @author Steven M
	 * @descripcion Ordena la lista de permisos concedidos de acuerdo al correo de
	 *              usuario destino de cada permiso
	 * @fecha 5/12/2018
	 */
	public void ordenarListaPermisos() {
		Collections.sort(this.listaPermisos, (a, b) -> a.getUsuarioOrigen().getCuenta().getCorreo()
				.compareToIgnoreCase(b.getUsuarioOrigen().getCuenta().getCorreo()));
	}

	public boolean isUsuLogueado() {
		return usuLogueado;
	}

	public void setUsuLogueado(boolean usuLogueado) {
		this.usuLogueado = usuLogueado;
	}

	public String getTipoPermisoSeleccionadoConceder() {
		return tipoPermisoSeleccionadoConceder;
	}

	public void setTipoPermisoSeleccionadoConceder(String tipoPermisoSeleccionadoConceder) {
		this.tipoPermisoSeleccionadoConceder = tipoPermisoSeleccionadoConceder;
	}

	public String getTipoLectura() {
		return tipoLectura;
	}

	public void setTipoLectura(String tipoLectura) {
		this.tipoLectura = tipoLectura;
	}

	public String getTipoEscritura() {
		return tipoEscritura;
	}

	public void setTipoEscritura(String tipoEscritura) {
		this.tipoEscritura = tipoEscritura;
	}

	public Usuario getUsuarioSesion() {
		return usuarioSesion;
	}

	public void setUsuarioSesion(Usuario usuarioSesion) {
		this.usuarioSesion = usuarioSesion;
	}

	public List<Permiso> getListaPermisos() {
		return listaPermisos;
	}

	public void setListaPermisos(List<Permiso> listaPermisos) {
		this.listaPermisos = listaPermisos;
	}

	public String getCorreoIngresado() {
		return correoIngresado;
	}

	public void setCorreoIngresado(String correoIngresado) {
		this.correoIngresado = correoIngresado;
	}

	public Permiso getPermisoABorrar() {
		return permisoABorrar;
	}

	public void setPermisoABorrar(Permiso permisoABorrar) {
		this.permisoABorrar = permisoABorrar;
	}

}
