package co.com.tracert.vtrack.web.named;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Dosis;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.EsquemaAgregado;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.entities.Vacuna;
import co.com.tracert.vtrack.model.entities.VacunaEsquema;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class EsquemasSugeridosNB extends NamedBeanUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6968846575145556393L;

	@EJB
	private DelegadoEJB delegado;

	private List<EsquemaDTO> listaEsq;

	private VacunaEsquema vacunaEsquema;

	protected Vacuna vacunaTmp;

	protected VacunaEsquema vacEsqTmp;

	protected int numDosis;

	protected int numRefuerzos;

	protected Esquema esquemaSeleccionado;

	protected Usuario usuario;

	/**
	 * Variable que permitira bloquer las funcionalidades si el usuario solo tiene
	 * permisos de lectura.
	 */
	private boolean bloquer_funcionalidad;

	/**
	 * Es la lista de usuarios a los cuales el usuario en sesión tiene permiso tanto
	 * para ver como editar su información
	 */
	private List<Permiso> listPermisos;

	// Constructor
	public EsquemasSugeridosNB() {

	}

	@PostConstruct
	public void init() {

		listaEsq = null;
		vacunaEsquema = null;
		this.bloquer_funcionalidad = false;

		try {
			// Asigna / inicializa a el usuario en sesión
			asignarUsuario();
			// Llama al método para inicializar la página
			actualizarPagina();
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
		}

	}

	/**
	 * @author Steven M
	 * @descripcion asigna a la var usuario el valor dependiendo de si hay o no
	 *              usuario Impersonate : 1. Correspondiente con el usuario que se
	 *              logeo 2. Correspondiente con el usuario impersonate
	 * @fecha 1/11/2018
	 */
	public void asignarUsuario() {
		Usuario usuarioImp = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
		// Si hay usuario impersonate entonces el usuario en sesión es el usuario
		// impersonate
		if (usuarioImp != null) {
			try {
				usuario = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);

				Usuario usuarioLogeado = (Usuario) getFromSession(ConstantesVtrack.USUARIO);

				this.listPermisos = delegado.darPermisosDestinoDeAprobado(usuarioLogeado);

				// Verificar que tipo de permisos tiene el usuario que se encuentra en
				// reemplazo. Si el tipo de permiso es Lectura y Escritura o de Lectura no
				// mostrará la vista del perfil de usuario
				Permiso permiso = obtenerPermisoSeleccionado(this.usuario.getIdUsuario());

				if (permiso.getTipoPermiso().equals(ConstantesVtrack.PERMISO_LECTURA)) {
					this.bloquer_funcionalidad = true;
				}

			} catch (Exception e) {
				addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
			}
		} else {
			usuario = (Usuario) getFromSession(ConstantesVtrack.USUARIO);

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

	/**
	 * @author Ana Maria A
	 * @descripcion Se agrega el esquema sugerido seleccionado al carné de
	 *              vacunación
	 * @fecha 25/9/2018
	 */
	public void agregarEsquemaVacunacion() {
		try {
			EsquemaAgregado esquemaAgregado = delegado.agregarEsquemaSugerido(usuario, esquemaSeleccionado);
			Usuario usuarioEnSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
			delegado.auditarAgregarEsquemaAlCarne(esquemaAgregado, usuarioEnSesion);
			addMessage(FacesMessage.SEVERITY_INFO,
					"Se agregó el esquema " + esquemaSeleccionado.getNombre() + " satisfactoriamente");
			actualizarPagina();
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, "No fue posible agregar el esquema de vacunacion");
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Permite inicializar (y actualizar ) toda la información de los
	 *              esquemas que pertenecen al carné del usuario
	 * @fecha 25/9/2018
	 */
	public void actualizarPagina() {
		try {
			// Lista de esquemas sugeridos para el usuario quemado
			listaEsq = delegado.sugerirEsquemasVacunacion(usuario, usuario.getPai());

		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Actualiza tanto el usuario de donde extrae la información como
	 *              la página
	 * @fecha 2/11/2018
	 */
	public void actualizarTodo() {
		this.asignarUsuario();
		this.actualizarPagina();
	}

	/**
	 * @author Steven M
	 * @descripcion Permite determinar el número de dosis tipo normal y refuerzo
	 * @fecha 20/9/2018
	 */
	public void mostrarInformacionVacuna() {
		numDosis = 0;
		numRefuerzos = 0;
		for (Dosis dosis : vacEsqTmp.getDosis()) {
			if (dosis.getTipoDosis().equalsIgnoreCase("D")) {
				numDosis++;
			}
			// A pesar de haber solo dos tipos, el sistema puede crecer
			if (dosis.getTipoDosis().equals("R")) {
				numRefuerzos++;
			}
		}
	}

	public int darDosisMaxima(Esquema esquema) throws Exception {
		return delegado.darDosisMaxima(esquema);

	}

	public DelegadoEJB getDelegado() {
		return delegado;
	}

	public void setDelegado(DelegadoEJB delegado) {
		this.delegado = delegado;
	}

	public List<EsquemaDTO> getListaEsq() {
		return listaEsq;
	}

	public void setListaEsq(List<EsquemaDTO> listaEsq) {
		this.listaEsq = listaEsq;
	}

	public VacunaEsquema getVacunaEsquema() {
		return vacunaEsquema;
	}

	public void setVacunaEsquema(VacunaEsquema vacunaEsquema) {
		this.vacunaEsquema = vacunaEsquema;
	}

	public Vacuna getVacunaTmp() {
		return vacunaTmp;
	}

	public void setVacunaTmp(Vacuna vacunaInfTmp) {
		this.vacunaTmp = vacunaInfTmp;
	}

	public Esquema getEsquemaSeleccionado() {
		return esquemaSeleccionado;
	}

	public void setEsquemaSeleccionado(Esquema esquemaSeleccionado) {
		this.esquemaSeleccionado = esquemaSeleccionado;
	}

	public VacunaEsquema getVacEsqTmp() {
		return vacEsqTmp;
	}

	public void setVacEsqTmp(VacunaEsquema vacEsqTmp) {
		this.vacEsqTmp = vacEsqTmp;
	}

	public int getNumDosis() {
		return numDosis;
	}

	public void setNumDosis(int numDosis) {
		this.numDosis = numDosis;
	}

	public int getNumRefuerzos() {
		return numRefuerzos;
	}

	public void setNumRefuerzos(int numRefuerzos) {
		this.numRefuerzos = numRefuerzos;
	}

	public boolean isBloquer_funcionalidad() {
		return bloquer_funcionalidad;
	}

	public void setBloquer_funcionalidad(boolean bloquer_funcionalidad) {
		this.bloquer_funcionalidad = bloquer_funcionalidad;
	}

	public List<Permiso> getListPermisos() {
		return listPermisos;
	}

	public void setListPermisos(List<Permiso> listPermisos) {
		this.listPermisos = listPermisos;
	}
	
	

}
