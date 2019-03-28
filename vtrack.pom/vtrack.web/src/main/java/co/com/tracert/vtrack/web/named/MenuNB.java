package co.com.tracert.vtrack.web.named;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Menu;
import co.com.tracert.vtrack.model.entities.MenuPagina;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class MenuNB extends NamedBeanUtils implements Serializable {

	//////////////////////////////////////////////////////////
	// Atributos
	//////////////////////////////////////////////////////////

	/**
	 * 
	 */
	private static final long serialVersionUID = -5769095773698978640L;

	/**
	 * 
	 */
	@EJB
	private DelegadoEJB delegado;

	/**
	 * Es la lista que contiene el nombre y el ícono de cada menú
	 */
	private List<Menu> listaMenu;

	/**
	 * Hace referencia a la url de la página actual
	 */
	private String paginaActual;

	/**
	 * Hace referencia al nombre de la página actual
	 */
	private String paginaActualTmp;

	/**
	 * Hace referencia
	 */
	private Usuario usuarioSesion;

	/**
	 * Lista de permisos en espera para mostrar el número de estos en el menú
	 */
	private List<Permiso> listPermisosEspera;

	/**
	 * Con este atributo se asegura que se muestre o no información del número de
	 * recibos recibidos false = no se muestra número de permisos recibidos true =
	 * se muestra el número de permisos recibidos
	 */
	private boolean usuLogueado;

	/**
	 * Con este atributo se asegura que se muestre el id del permiso recibidos
	 */
	private Long idPermisosRecibidos;

	/**
	 * Es la lista de usuarios a los cuales el usuario en sesión tiene permiso tanto
	 * para ver como editar su información
	 */
	private List<Permiso> listPermisos;

	private Permiso permiso;

	//////////////////////////////////////////////////////////
	// Constructor
	//////////////////////////////////////////////////////////

	/**
	 * @descripcion
	 * 
	 */
	public MenuNB() {

	}

	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		// Se inicializa la var usuLogueado dependiendo del usuario escogido en el
		// Header
		asignarBooleanUsuarioEnSesion();
		paginaActual = ConstantesVtrack.PAGINA_INICIO;
		// Se inicializa el id del menu con permisos recibidos
		idPermisosRecibidos = ConstantesVtrack.ID_PERMISOS_RECIBIDOS;
		try {
			organizarListaMenu();
			listPermisosEspera = delegado.darPermisosDestinoDeEnEspera(usuarioSesion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Permite ordenar la lista menu de acuerdo a su idMenu
	 * @fecha 1/10/2018
	 */
	public void organizarListaMenu() {

		Menu menuTmp;
		for (int i = 0; i < listaMenu.size(); i++) {

			for (int j = 0; j < listaMenu.size() - 1; j++) {
				if (listaMenu.get(j).getIdMenu() > listaMenu.get(j + 1).getIdMenu()) {
					menuTmp = listaMenu.get(j + 1);
					listaMenu.remove(j + 1);
					listaMenu.add(j, menuTmp);
				}
			}
		}
	}

	/**
	 * @author Steven Muriel
	 * @descripcion Cambia de página dependiendo del menú seleccionado
	 * @fecha 2/11/2018
	 * @return retorna el url de la página que corresponde con el nombre del
	 *         sub-menú seleccionado
	 */
	public String menuSeleccionado() {
		boolean paginaDeterminada = false;

		for (int i = 0; i < listaMenu.size() && !paginaDeterminada; i++) {
			Menu menuActual = listaMenu.get(i);
			String nombreMenuActual = menuActual.getNombre();
			if (nombreMenuActual.equals(paginaActualTmp)) {
				List<MenuPagina> menuPaginas = menuActual.getMenuPaginas();
				if (menuPaginas.isEmpty()) {
					addMessage(FacesMessage.SEVERITY_INFO, "No implementada aún");
				} else {
					paginaActual = menuPaginas.get(0).getPagina().getUrl();
				}
				paginaDeterminada = true;
			}
		}

		// Cuando el menú es cerrar sesión se debe hacer un paso adicional
		if (paginaActual.equals(ConstantesVtrack.PAGINA_LOGIN_SIMPLE + ConstantesVtrack.REDIRECION_PAGINAS)) {
			// Se invalida la sesión actual
			Usuario usuarioEnsesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
			Cuenta cuentaEnSesionAnterior = usuarioEnsesion.getCuenta();
			Cuenta cuentaCerrarSesionActual= usuarioEnsesion.getCuenta();
			try {
				//Como estoy modificando solo la fecha, no debe salir excepcion
				cuentaCerrarSesionActual = delegado.actualizarFechaCerrarSesion(cuentaCerrarSesionActual);
				delegado.auditarCerrarSesion(cuentaEnSesionAnterior, cuentaCerrarSesionActual);
			} catch (Exception e) {
				addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			}
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

			
		}
		return paginaActual;
	}

	/**
	 * @author Steven M
	 * @descripcion asigna a la var usuLogueado true o false dependiendo del usuario
	 *              escogido en el Header
	 * @fecha 5/12/2018
	 */
	public void asignarBooleanUsuarioEnSesion() {
		Usuario usuarioImp = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
		try {
			if (usuarioImp != null) {
				usuarioSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
				Usuario usuarioLogeado = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
				this.listPermisos = delegado.darPermisosDestinoDeAprobado(usuarioLogeado);
				// Verificar que tipo de permisos tiene el usuario que se encuentra en
				// reemplazo. Si el tipo de permiso es Lectura y Escritura o de Lectura no
				// mostrará la vista del perfil de usuario
				permiso = obtenerPermisoSeleccionado(this.usuarioSesion.getIdUsuario());
				listaMenu = delegado.consultarMenuUsuario(usuarioSesion);
				usuLogueado = false;
			} else {
				usuarioSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
				listaMenu = delegado.consultarMenuUsuario(usuarioSesion);
				usuLogueado = true;
			}
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
		}
	}

	public boolean verificar(Menu menu) {
		if (permiso != null) {
			if (permiso.getTipoPermiso().equals(ConstantesVtrack.PERMISO_LECTURA)) {
				if (menu.getIdMenu() == 1L || menu.getIdMenu() == 7L) {
					return false;
				} else {
					return true;
				}
			}
			if (permiso.getTipoPermiso().equals(ConstantesVtrack.PERMISO_ESCRITURA)) {
				if (menu.getIdMenu() == 1L || menu.getIdMenu() == 7L || menu.getIdMenu() == 2L) {
					return false;
				} else {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * @author Steven M
	 * @descrpcion Busca el permiso que se selecciono en la lista de select one menu
	 *             a partir de su id
	 * @param id del usuario que se busca en la lista del NB permisos
	 * @return
	 */
	public Permiso obtenerPermisoSeleccionado(Long id) {
		Permiso permiso = null;
		boolean encontro = false;
		for (int i = 0; i < listPermisos.size() && !encontro; i++) {
			if (listPermisos.get(i).getUsuarioDestino().getIdUsuario() == id) {
				permiso = listPermisos.get(i);
				encontro = true;
			}
		}
		return permiso;
	}

	public void bloquearMenu() {
		for (int i = 0; i < listaMenu.size(); i++) {
		}
	}

	public DelegadoEJB getDelegado() {
		return delegado;
	}

	public void setDelegado(DelegadoEJB delegado) {
		this.delegado = delegado;
	}

	public List<Menu> getListaMenu() {
		return listaMenu;
	}

	public void setListaMenu(List<Menu> listaMenu) {
		this.listaMenu = listaMenu;
	}

	public String getPaginaActual() {
		return paginaActual;
	}

	public void setPaginaActual(String paginaActual) {
		this.paginaActual = paginaActual;
	}

	public String getPaginaActualTmp() {
		return paginaActualTmp;
	}

	public void setPaginaActualTmp(String paginaActualTmp) {
		this.paginaActualTmp = paginaActualTmp;
	}

	public Usuario getUsuarioSesion() {
		return usuarioSesion;
	}

	public void setUsuarioSesion(Usuario usuarioSesion) {
		this.usuarioSesion = usuarioSesion;
	}

	public List<Permiso> getListPermisosEspera() {
		return listPermisosEspera;
	}

	public void setListPermisosEspera(List<Permiso> listPermisosEspera) {
		this.listPermisosEspera = listPermisosEspera;
	}

	public boolean isUsuLogueado() {
		return usuLogueado;
	}

	public void setUsuLogueado(boolean usuLogueado) {
		this.usuLogueado = usuLogueado;
	}

	public Long getIdPermisosRecibidos() {
		return idPermisosRecibidos;
	}

	public void setIdPermisosRecibidos(Long idPermisosRecibidos) {
		this.idPermisosRecibidos = idPermisosRecibidos;
	}

}
