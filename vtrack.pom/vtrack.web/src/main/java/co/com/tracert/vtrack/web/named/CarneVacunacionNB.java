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

import org.primefaces.PrimeFaces;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.dto.ColumnasVacunaDTO;
import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.Dosis;
import co.com.tracert.vtrack.model.entities.DosisAplicada;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.entities.Vacuna;
import co.com.tracert.vtrack.model.entities.VacunaEsquema;
import co.com.tracert.vtrack.model.entities.VacunaOculta;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class CarneVacunacionNB extends NamedBeanUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2898795975328623718L;

	/**
	 * 
	 */
	@EJB
	private DelegadoEJB delegado;

	private List<EsquemaDTO> listaEsq;

	private Vacuna vacunaTmp;

	private Vacuna vacunaBorrar;

	private VacunaEsquema vacEsqTmp;

	private int numDosis;

	private int numRefuerzos;

	private Esquema esquemaSeleccionado;

	private ColumnasVacunaDTO dosisAAplicar;

	private ColumnasVacunaDTO[] listaDosisDTO;

	/**
	 * Representa el valor que toma el switch dentro del dialogo. Tal switch se
	 * utiliza para confirmar si se quiere registrar la dosis o no.
	 */
	private boolean switchRegistrarDosis;

	/**
	 * Es una cadena que representa la dosis relacionada con el radioButton que
	 * presiona el usuario dentro de un esquema del carnet.
	 */
	private String dosisSeleccionada;

	/**
	 * Representa la fecha de aplicacion de la dosis seleccionada.
	 */
	private Date fechaAplicacion;

	/**
	 * Valor que determina si el calendario estará habilitado o no.
	 */
	private Boolean calendarioActivo;

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

	protected Usuario usuario;

	private boolean render_mensaje;

	private String nombre_cuenta;

	/**
	 * 
	 */
	public CarneVacunacionNB() {

	}

	@PostConstruct
	private void init() {
		listaEsq = null;
		vacunaTmp = null;
		bloquer_funcionalidad = false;
		render_mensaje = false;
		try {
			// Asigna / inicializa a el usuario en sesión
			asignarusuario();

			// Llama al método para inicializar la página
			actualizarPagina();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"No se pudo construir el carné de vacunacion: " + e.getMessage()));
		}
	}

	/**
	 * @author Steven M
	 * @descripcion asigna a la var usuario el valor: 1. Correspondiente con el
	 *              usuario que se logeo 2. Correspondiente con el usuario
	 *              impersonate
	 * @fecha 1/11/2018
	 */
	public void asignarusuario() {
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
					this.render_mensaje = true;
					this.nombre_cuenta = permiso.getNombreCuenta();
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
	 * @author Steven M
	 * @descripcion Permite inicializar (y actualizar ) toda la información de los
	 *              esquemas que pertenecen al carné del usuario
	 * @fecha 25/9/2018
	 */
	public void actualizarPagina() {
		try {
			// Lista de carne de vacunación para el usuario en sesión
			listaEsq = delegado.darCarneVacunacion(usuario);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
					"Error inicianlizando Esquemas Sugeridos" + e.getMessage()));
		}
	}

	/**
	 * @author Steven M
	 * @descripcion Actualiza tanto el usuario de donde extrae la información como
	 *              la página
	 * @fecha 2/11/2018
	 */
	public void actualizarTodo() {
		this.asignarusuario();
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

	/**
	 * @author Ana Arango
	 * @descripcion Borra la vacuna seleccionada
	 * @fecha 10/11/2018
	 */
	public void borrarVacunaSeleccionada() {
		try {
			VacunaOculta vacunaOculta = delegado.ocultarVacunaCarne(usuario, vacunaBorrar);
			Usuario usuarioEnSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
			delegado.auditarEliminarVacunaDeEsquemaEnCarne(vacunaOculta, usuarioEnSesion);
			delegado.verificarEsquemaAgregadoVacio(usuario, esquemaSeleccionado);
			actualizarPagina();
			addMessage(FacesMessage.SEVERITY_INFO, "Se eliminó exitosamente la vacuna ");
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, "Excepcion en ocultar vacuna: " + e.getMessage());
		}

	}

	/**
	 * @author Steven M
	 * @descripcion redirige a la vista de gestion de esquemas sugeridos
	 * @fecha 20/9/2018
	 * @return url que redirige a la página de esquemas sugeridos
	 */
	public String irAEsquemasSugeridos() {
		return ConstantesVtrack.PAGINA_INICIO + ConstantesVtrack.REDIRECION_PAGINAS;
	}

	/**
	 * @author Santiago Restrepo Prado
	 * @descripcion Metodo que se encarga de recibir el evento generado al oprimir
	 *              el boton Confirmar dentro del dialogo Registro de Dosis.
	 */
	public void accionDialogoConfirmar() {
		DosisAplicada dosisAplicada = dosisAAplicar.getDosisAplicada();
		try {
			if (dosisAplicada == null) {
				if (switchRegistrarDosis == ConstantesVtrack.SWITCH_ON) {
					if (!verificarFechaDeAplicacionDeDosisAnterior(dosisAAplicar)) {
						throw new Exception(
								"La fecha ingresada no puede ser anterior a la fecha de la última dosis registrada de la vacuna");
					}
					delegado.registrarAplicacionDosis(usuario, dosisAAplicar.getDosis(), fechaAplicacion);
					addMessage(FacesMessage.SEVERITY_INFO, "La dosis ha sido registrada con éxito!");
					actualizarPagina();
				}
			} else {
				String estado = (switchRegistrarDosis) ? ConstantesVtrack.ESTADO_ACTIVO
						: ConstantesVtrack.ESTADO_INACTIVO;

				if (estado.equals(ConstantesVtrack.ESTADO_ACTIVO)
						&& !verificarFechaDeAplicacionDeDosisAnterior(dosisAAplicar)) {
					throw new Exception(
							"La fecha ingresada no puede ser anterior a la fecha de la última dosis registrada de la vacuna");
				}
				if ((estado.equals(ConstantesVtrack.ESTADO_INACTIVO) && !dosisAAplicar.getPuedeEliminarse())
						&& dosisAplicada.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO)) {
					addMessage(FacesMessage.SEVERITY_INFO,
							"No puedes eliminar esta dosis sin haber eliminado las dosis posteriores");
				} else {
					DosisAplicada dosisAplicadaAnterior = dosisAAplicar.getDosisAplicada();
					Date fechaAnterior = dosisAplicada.getFechaAplicacion();
					dosisAplicada.setFechaAplicacion(fechaAplicacion);
					DosisAplicada dosisAplicadaActual = delegado.modificarAplicacionDosis(dosisAplicada, estado);
					addMessage(FacesMessage.SEVERITY_INFO, "La dosis ha sido actualizada con éxito!");
					Usuario usuarioEnSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
					dosisAplicadaAnterior.setFechaAplicacion(fechaAnterior);
					delegado.auditarModificarAplicacionDosis(dosisAplicadaAnterior, dosisAplicadaActual, usuarioEnSesion);
					actualizarPagina();
				}
			}
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @author Santiago R, Steven M
	 * @descripcion Abre el dialogo donde se gestiona la aplicación de dosis
	 * @fecha 3/11/2018
	 */
	public void accionMostrarDialogoGestionDosis() {
		DosisAplicada aplicada = dosisAAplicar.getDosisAplicada();
		if (aplicada != null) {
			switchRegistrarDosis = (aplicada.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO)) ? true
					: false;

			if (aplicada.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {
				fechaAplicacion = new Date(System.currentTimeMillis());
			} else {
				fechaAplicacion = dosisAAplicar.getDosisAplicada().getFechaAplicacion();
			}

		} else {
			switchRegistrarDosis = false;
			fechaAplicacion = new Date(System.currentTimeMillis());
		}
		calendarioActivo = (switchRegistrarDosis == ConstantesVtrack.SWITCH_ON) ? false : true;

		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogoRegistrarDosis').show()");
	}

	/**
	 * @author Santiago Restrepo
	 * @descripcion Método que se encarga de habilitar o deshabilitar el calendario.
	 *              Si el switch está en ON el calendario se habilita; si está en
	 *              OFF el calendario se deshabilita. OJO: la propiedad que cambia
	 *              del calendario se llama "disabled", en consecuencia su
	 *              disposición es el valor contrario al valor que tenga el switch.
	 * @fecha 3/11/2018
	 */
	public void actualizarDisposicionCalendario() {
		calendarioActivo = (switchRegistrarDosis == ConstantesVtrack.SWITCH_ON) ? false : true;
	}

	/**
	 * @author Santiago Restrepo
	 * @descripcion Método que cierra el diálogo para gestionar dosis vía botón
	 *              Cancelar.
	 * @fecha 3/11/2018
	 */
	public void cerrarDialogo() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogoRegistrarDosis').hide()");
	}

	/**
	 * @author Steven M
	 * @descripcion Método que cierra el diálogo de confirmar borrar vacuna
	 * @fecha 6/11/2018
	 */
	public void cerrarConfirmacion() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('confirmarDialogo').hide()");
	}

	/**
	 * @author Steven M, Santiago Restrepo
	 * @descripcion A partir de la variable temporal de la vacuna que es pasada por
	 *              parámetro, se determina el color del botón que debe aparecer en
	 *              el xhtml. GRIS: dosis aplicada es null o dosis aplicada no es
	 *              null y su estado es inactivo VERDE: dosis aplicada no es null y
	 *              su estado es activo
	 * @param vacuna es la variable temporal que está siendo pintada en el xhtml y
	 *               necesitamos saber qué color del botón el corresponde.
	 * @fecha 5/11/2018
	 * @return String correspondiente al color del botón
	 */
	public String darColorBoton(ColumnasVacunaDTO vacuna) {
		String color = "";
		DosisAplicada aplicada = vacuna.getDosisAplicada();
		if (aplicada == null || aplicada.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_INACTIVO)) {
			color = ConstantesVtrack.COLOR_INACTIVO;
		} else {
			color = ConstantesVtrack.COLOR_ACTIVO;
		}
		return color;
	}

	/**
	 * @author Ana Arango
	 * @descripcion Verifica que la dosis que se desea a aplicar no tenga una fecha
	 *              de aplicación anterior a la de la dosis anterior
	 * @fecha 10/11/2018
	 */
	public boolean verificarFechaDeAplicacionDeDosisAnterior(ColumnasVacunaDTO dosisDTO) {
		boolean puede = true;
		int indice = indiceDe(dosisDTO);
		if (indice > 0 && listaDosisDTO[indice - 1] != null) {
			ColumnasVacunaDTO anterior = listaDosisDTO[indice - 1];
			if (anterior.getDosisAplicada().getFechaAplicacion().after(fechaAplicacion)) {
				puede = false;
			}

		}

		return puede;
	}

	private int indiceDe(ColumnasVacunaDTO dosisDTO) {
		int indice = -1;
		boolean encontr = false;
		for (int i = 0; i < listaDosisDTO.length && !encontr; i++) {
			if (listaDosisDTO[i].equals(dosisDTO)) {
				indice = i;
				encontr = true;
			}
		}

		return indice;
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

	public Vacuna getVacunaBorrar() {
		return vacunaBorrar;
	}

	public void setVacunaBorrar(Vacuna vacunaBorrar) {
		this.vacunaBorrar = vacunaBorrar;
	}

	public Vacuna getVacunaTmp() {
		return vacunaTmp;
	}

	public void setVacunaTmp(Vacuna vacunaTmp) {
		this.vacunaTmp = vacunaTmp;
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

	public boolean isSwitchRegistrarDosis() {
		return switchRegistrarDosis;
	}

	public void setSwitchRegistrarDosis(boolean switchRegistrarDosis) {
		this.switchRegistrarDosis = switchRegistrarDosis;
	}

	public String getDosisSeleccionada() {
		return dosisSeleccionada;
	}

	public void setDosisSeleccionada(String dosisSeleccionada) {
		this.dosisSeleccionada = dosisSeleccionada;
	}

	public ColumnasVacunaDTO getDosisAAplicar() {
		return dosisAAplicar;
	}

	public void setDosisAAplicar(ColumnasVacunaDTO dosisAAplicar) {
		this.dosisAAplicar = dosisAAplicar;
	}

	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public Boolean getCalendarioActivo() {
		return calendarioActivo;
	}

	public void setCalendarioActivo(Boolean calendarioActivo) {
		this.calendarioActivo = calendarioActivo;
	}

	public ColumnasVacunaDTO[] getListaDosisDTO() {
		return listaDosisDTO;
	}

	public void setListaDosisDTO(ColumnasVacunaDTO[] listaDosisDTO) {
		this.listaDosisDTO = listaDosisDTO;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isRender_mensaje() {
		return render_mensaje;
	}

	public void setRender_mensaje(boolean render_mensaje) {
		this.render_mensaje = render_mensaje;
	}

	public String getNombre_cuenta() {
		return nombre_cuenta;
	}

	public void setNombre_cuenta(String nombre_cuenta) {
		this.nombre_cuenta = nombre_cuenta;
	}

}