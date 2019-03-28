package co.com.tracert.vtrack.web.named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Estado;
import co.com.tracert.vtrack.model.entities.Notificacion;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Region;
import co.com.tracert.vtrack.model.entities.TipoDocumentoPais;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class CrearCuentaAsociadaNB extends NamedBeanUtils implements Serializable {

	//////////////////////////////////////////////////////////
	// Atributos
	//////////////////////////////////////////////////////////

	/**
	 * Serial generado
	 */
	private static final long serialVersionUID = 4546445427887493680L;

	/**
	 * Delegado que se conectara con la lógica
	 */
	@EJB
	private DelegadoEJB delegado;

	/**
	 * Variable que permitirá manejar el número de documento que identificará al
	 * usuario
	 */
	private String numeroDocumento;

	/**
	 * Variable que permitirá manejar el tipo de documento que posee el usuario
	 */
	private String tipoDocumento;

	/**
	 * Variable que permitirá manejar el nombre del usuario
	 */
	private String nombre;

	/**
	 * Variable que permitirá manejar el género del usuario
	 */
	private String sexo;

	/**
	 * Variable que permitirá manejar la correspondiente fecha de nacimiento del
	 * usuario
	 */
	private Date fecha_Nacimiento = new Date();

	/**
	 * Variable que permitirá manejar el pais de residencia del usuario
	 */
	private String pais;

	/**
	 * Variable que permitirá manejar en la vista, a través de un SelectItem por
	 * defecto del SelectOneMenu del pais
	 */
	private Pais pais_defecto;

	/**
	 * Variable que permitirá manejar la región de residencia del usuario
	 */
	private String region;

	/**
	 * Variable que permitirá manejar en la vista, a través de un SelectItem por
	 * defecto del SelectOneMenu de la región
	 */
	private Region region_defecto;

	private String parentezco;

	/**
	 * Variable que permitirá manejar la ciudad de residencia del usuario
	 */
	private String ciudad;

	/**
	 * Variable que permitirá manejar el código postal del usuario
	 */
	private String codigoPostal;

	/**
	 * Variable que permitirá manejar en la vista, la lista de paises que se
	 * encuentran en el sistema de Vtrack
	 */
	private List<Pais> listaPaises;

	/**
	 * Variable que permitirá manejar en la vista, la lista de regiones que se
	 * encuentran en el sistema de Vtrack
	 */
	private List<Region> listaRegiones;

	/**
	 * Variable que permitirá manejar en la vista, la lista de ciudades que se
	 * encuentran en el sistema de Vtrack
	 */
	private List<Ciudad> listaCiudades;

	/**
	 * Variable que permitirá manejar en la vista, la lista de géneros que se
	 * encuentran en el sistema de Vtrack
	 */
	private List<String> lstGeneros;

	/**
	 * Variable que permitirá manejar en la vista, la lista los tipos de documento
	 * de el pais de residencia del usuario
	 */
	private List<TipoDocumentoPais> listaTipoDocumento;

	/**
	 * Variable que permitirá manejar en la vista, a través de un placeholder y
	 * outLabel la distribución geografica del pais de residencia del usuario
	 */
	private String textoRegion;

	/**
	 * Variable que permitirá bloquear el boton de crear una cuenta asociada del
	 * respectivo usuario en sesión
	 */
	private Boolean habilitarBoton;

	/**
	 * Variable que permitirá manejar el usuario que se encuentra actualmente en
	 * sesión
	 */
	private Usuario usuarioSesion;

	/**
	 * Variable que permitirá manejar el SelectItem por defecto Invalido de la
	 * ciudad seleccionada
	 */
	private Ciudad ciudad_seleccionada;

	/**
	 * Variable que permitirá manejar el SelectItem por defecto Invalido de la
	 * región seleccionada
	 */
	private Region region_seleccionada;

	/**
	 * Variable que permitirá bloquear los dias del calendario desde hoy
	 */
	private Date hoy;

	/**
	 * Variable que permitirá escribir el mensaje de necesidad del número de
	 * documento
	 */
	private String marcaAgua_numero;

	/**
	 * Variable que permitirá escribir el mensaje de necesidad del tipo de documento
	 */
	private String marcaAgua_tipo;

	/**
	 * Variable que permitirá mostrar el mensaje de necesidad del número de
	 * documento
	 */
	private boolean render_numero;

	/**
	 * Variable que permitirá mostrar el mensaje de necesidad del tipo de documento
	 */
	private boolean render_tipo;

	private boolean render_vista;

	//////////////////////////////////////////////////////////
	// Constructor
	//////////////////////////////////////////////////////////

	/**
	 * @descripcion Constructor de NamedBean que referencia a la vista
	 *              cuentaAsociada.xhtml
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	public CrearCuentaAsociadaNB() {

	}

	@PostConstruct
	private void init() {
		// Obtiene el usuario en sesion
		asignarUsuarioSesion();
		// Se inicializan las variables de control
		activarVariablesDeControl();
		try {
			// Método para inicializar los géneros
			inicializarGeneros();
			// Inicializar todos los paises
			inicializarPais();
			// Inicializa los tipos de documentos
			inicializarTiposDocumentos();
			// Inicializar las regiones
			iniciarlizarRegiones();
			// Inicializar las ciudades
			iniciarlizarCiudades();

		} catch (Exception e) {
			// Exception controlada
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}

	}

	/**
	 * @descripcion Método que permitirá la inicializacion de las variables de
	 *              control de la vista
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	private void activarVariablesDeControl() {
		// Se instancia la propiedad de habilitar un boton para que este inicie
		// desactivado
		this.habilitarBoton = true;
		// Se inicializa el texto de la región.
		this.textoRegion = "Región: ";
		// Se inicializa el mensaje que muestra la necesidad del número de documento
		this.marcaAgua_numero = "Ingresar número de documento";
		// Se inicializa el mensaje que muestra la necesidad del tipo de documento
		this.marcaAgua_tipo = "Ingresar tipo de documento";
		// Se inicializa los componentes de renderización del numero de documento
		this.render_numero = false;
		// Se inicializa los componentes de renderización del tipo de documento
		this.render_tipo = false;
	}

	/**
	 * @author Steven M
	 * @descripcion asigna a la var usuarioSesion el valor: 1. Correspondiente con
	 *              el usuario que se logeo 2. Correspondiente con el usuario
	 *              impersonate
	 * @fecha 1/11/2018
	 */
	public void asignarUsuarioSesion() {
		Usuario usuarioImp = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
		// Si hay usuario impersonate entonces el usuario en sesión es el usuario
		// impersonate
		if (usuarioImp != null) {
			usuarioSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
			this.render_vista = false;
		} else {
			usuarioSesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
			this.render_vista = true;
		}
	}

	/**
	 * @descripcion Método que permitirá la inicialización de los componentes de la
	 *              lista de tipos de documentos
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	private void inicializarTiposDocumentos() {
		this.listaTipoDocumento = new ArrayList<TipoDocumentoPais>();
		try {
			// Inicializa la lista de tipos documentos del pais por defecto Colombia
			this.listaTipoDocumento = delegado.listarTipoDocumentos(this.pais_defecto);
			Collections.sort(this.listaTipoDocumento,
					(a, b) -> a.getTipoDocumento().getNombre().compareToIgnoreCase(b.getTipoDocumento().getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @descripcion Método que permitirá la inicialización de los componentes de de
	 *              la lista de paises
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	private void inicializarPais() {
		this.listaPaises = new ArrayList<Pais>();
		try {
			// Inicializa la lista de los paises en el sistema Vtrack
			// Para esta versión solo aparecerán Colombia
			this.pais_defecto = delegado.obtenerPais(ConstantesVtrack.PAIS_DEFECTO);
			this.listaPaises = new ArrayList<Pais>();
			this.listaPaises.add(this.pais_defecto);
			Collections.sort(this.listaPaises, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @descripcion Método que permitirá la inicialización de los componentes de de
	 *              la lista de regiones
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	private void iniciarlizarRegiones() {
		// Se inicializa en null las regiones del Sistema Vtrack
		this.listaRegiones = new ArrayList<Region>();
		this.region_defecto = new Region();
		Long regionId = null;
		if (this.region != null) {
			regionId = Long.parseLong(this.region);
			this.region_defecto.setIdRegion(regionId);
		} else {
			this.region_defecto.setIdRegion(ConstantesVtrack.REGION_DEFECTO);
			this.region_seleccionada = new Region();
			this.region_seleccionada.setNombre("Ingrese " + this.textoRegion);
		}
		try {
			this.textoRegion = this.pais_defecto.getDistribucionRegional();
			this.listaRegiones = delegado.listarRegiones(this.pais_defecto);
			Collections.sort(this.listaRegiones, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @descripcion Método que permitirá la inicialización de los componentes de de
	 *              la lista de ciudades
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	private void iniciarlizarCiudades() {

		// Se inicializa en null las ciudades del Sistema Vtrack
		this.listaCiudades = new ArrayList<Ciudad>();
		Long regionId = null;
		if (this.region != null) {
			regionId = Long.parseLong(this.region);
			region_defecto.setIdRegion(regionId);
		} else {
			region_defecto.setIdRegion(ConstantesVtrack.REGION_DEFECTO);
		}
		try {
			listaCiudades = delegado.listarCiudades(region_defecto);
			Collections.sort(this.listaCiudades, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @description Método auxilir que lista los géneros que se mostraran en el
	 *              sistema.
	 * @fecha 15/01/2018
	 */
	private void inicializarGeneros() {
		// Se inicializa en null los generos del Sistema Vtrack
		this.lstGeneros = new ArrayList<String>();
		this.lstGeneros.add(ConstantesVtrack.MASCULINO);
		this.lstGeneros.add(ConstantesVtrack.FEMENINO);
		Collections.sort(this.lstGeneros, (a, b) -> a.compareToIgnoreCase(b));
	}

	/**
	 * @autor Cristian Rodríguez
	 * @description Método auxilir que permitirá habilitar el boton de creación.
	 *              Hasta que los campos de Nombre, Género, Fecha de nacimiento y el
	 *              parentezco del usuario
	 * @fecha 15/01/2018
	 */
	public void habilitarCrearCuenta() {
		Usuario usuarioImp = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
		// Si hay usuario impersonate entonces el usuario en sesión es el usuario
		// impersonate
		if (usuarioImp == null && this.nombre != null && !this.nombre.equals("") && this.sexo != null
				&& !this.sexo.equals("") && fecha_Nacimiento != null && this.parentezco != null
				&& !this.parentezco.equals("")) {
			this.habilitarBoton = false;
		} else {
			this.habilitarBoton = true;
		}

	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Obtiene la ciudad de una región previamente seleccionada
	 * @fecha 15/01/2018
	 */
	public void seleccionarCiudad() {
		Region region = new Region();
		// Se toma el identificador de la región para confirmar la ciudad a seleccionar
		Long regionId = Long.parseLong(this.region);
		region.setIdRegion(regionId);
		this.ciudad_seleccionada = new Ciudad();
		this.ciudad_seleccionada.setNombre("Ingrese Ciudad");
		this.ciudad_seleccionada.setIdCiudad(ConstantesVtrack.INVALIDO);
		try {
			listaCiudades = delegado.listarCiudades(region);
			Collections.sort(this.listaCiudades, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Obtiene la región de un pais previamente seleccionado.
	 * @fecha 15/01/2018
	 */
	public void seleccionarRegion() {
		// Cambiar el tipo de documentos al pais seleccionado. Si el nombre de la región
		// cambia.
		modificarTipoDocumento();

		Pais pais = new Pais();
		if (this.pais.equals(ConstantesVtrack.INVALIDO + "")) {

			this.textoRegion = "región";
			this.listaRegiones = null;

			this.region_seleccionada = new Region();
			this.region_seleccionada.setNombre("Ingrese región");
			this.region_seleccionada.setIdRegion(ConstantesVtrack.INVALIDO);
		} else {
			// Se toma el identificador del pais para confirmar la región a seleccionar
			Long paisId = Long.parseLong(this.pais);
			pais.setIdPais(paisId);
			try {
				// Modificar la distribución del pais seleccionado
				pais = delegado.obtenerPais(paisId);
				this.textoRegion = pais.getDistribucionRegional();
				this.listaRegiones = delegado.listarRegiones(pais);

				this.region_seleccionada = new Region();
				this.region_seleccionada.setNombre("Ingrese " + pais.getDistribucionRegional());
				this.region_seleccionada.setIdRegion(ConstantesVtrack.INVALIDO);

				Collections.sort(this.listaRegiones, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
			} catch (Exception e) {
				addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			}
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Modifica el documento de acuerdo al pais seleccionado. Cambiara
	 *              el tipo de documento si el pais seleccionado es diferente a
	 *              Colombia. El campo de región tambien se cambiara
	 * @fecha 15/01/2018
	 */
	private void modificarTipoDocumento() {
		Pais pais = new Pais();
		pais.setIdPais(ConstantesVtrack.TIPODOCUMENTO_PAIS);
		try {
			this.listaTipoDocumento = delegado.listarTipoDocumentos(pais);
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permitirá realizar el registro de la cuenta asociada.
	 *              Se obtien la información del usuario asociado y se le asigna los
	 *              permisos du su usuario padre
	 * @fecha 15/01/2018
	 */
	public void realizarRegistro() {
		// Envia la información del registro para las validaciones de las mismas
		registrarCuenta();
		// Limpia todos los campos despues del registro
		limpiarDatos();
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Realiza el mapeo de la información ingresada para crear una
	 *              cuenta y su respectivo usuario asociado. Para registrarla cuenta
	 *              asociada
	 * @fecha 15/01/2018
	 */
	private void registrarCuenta() {

		// Instancia el usuario
		Usuario usuario = new Usuario();
		// Obtener la información ingresado por el cliente del sistema.
		usuario.setNombre(this.nombre);
		usuario.setNumeroDocumento(this.numeroDocumento);
		usuario.setFechaNacimiento(this.fecha_Nacimiento);
		usuario.setZipCode(this.codigoPostal);

		// Filtra el campo del géneroS
		// Obtiene el campo del géneros
		if (sexo != null) {
			if (sexo.equals(ConstantesVtrack.MASCULINO)) {
				usuario.setGenero(ConstantesVtrack.LETRA_MASCULINO);
			} else {
				usuario.setGenero(ConstantesVtrack.LETRA_FEMENINO);
			}
		} else {
			usuario.setGenero(null);
		}
		// Se busca que si el cliente no ingreso algun dato. Se envia como elemento nulo
		// el tipo de documento.
		TipoDocumentoPais tipoDocumentoPais = new TipoDocumentoPais();
		Long idTipoDocPais = null;
		if (this.tipoDocumento != null) {
			idTipoDocPais = Long.parseLong(this.tipoDocumento);
			tipoDocumentoPais.setIdTipoDocPais(idTipoDocPais);
			usuario.setTipoDocumentoPai(tipoDocumentoPais);
		} else {
			usuario.setTipoDocumentoPai(null);
		}
		// Se busca que si el cliente no ingreso algun dato. Se envia como elemento nulo
		// la ciudad.
		Ciudad ciudad = new Ciudad();
		Long idCiudad = null;

		// Modifica la ciudad del usuario
		if (this.ciudad != null && !this.ciudad.equals(ConstantesVtrack.INVALIDO + "")) {
			idCiudad = Long.parseLong(this.ciudad);
			ciudad.setIdCiudad(idCiudad);
			usuario.setCiudad(ciudad);
		} else {
			usuario.setCiudad(null);
		}

		// Modifica la region del usuario
		Long idRegion = null;
		if (this.region != null && !this.region.equals(ConstantesVtrack.INVALIDO + "")) {
			Region regionTemp = new Region();
			idRegion = Long.parseLong(this.region);
			regionTemp.setIdRegion(idRegion);
			this.usuarioSesion.setRegion(regionTemp);
		} else {
			this.usuarioSesion.setRegion(null);
		}

		// Modifica el pais del usuario
		Long idPais = null;
		if (this.pais != null && !this.pais.equals(ConstantesVtrack.INVALIDO + "")) {
			Pais paisTemp = new Pais();
			idPais = Long.parseLong(this.pais);
			paisTemp.setIdPais(idPais);
			this.usuarioSesion.setPai(paisTemp);
		} else {
			this.usuarioSesion.setPai(null);
		}

		try {

			// Inicia en sesión al usuario recientemente creado
			// Mostrar el dialogo de registro correcto

			Permiso permiso = new Permiso();
			permiso.setNombreCuenta(this.parentezco);
			permiso.setUsuarioOrigen(this.usuarioSesion);
			permiso.setUsuarioDestino(usuario);
			permiso.setTipoPermiso(ConstantesVtrack.PERMISO_ASOCIADO);
			Estado estado = new Estado();
			estado.setEstado(ConstantesVtrack.ESTADO_APROBADO);
			permiso.setEstado(estado);
			Usuario asociado = delegado.crearUsuarioAsociado(permiso);
			// creamos la notificacion
			Notificacion notificacion = new Notificacion();
			notificacion.setDiasAnticipacion(new BigDecimal(ConstantesVtrack.NOTIFICAR_DIAS_ANTICIPACION));
			notificacion.setUsuario(asociado);
			notificacion = delegado.crearNotificacion(notificacion);
			// asociamos la notificacion al asociado
			asociado.setNotificacion(notificacion);
			Usuario usuarioAsociado = delegado.modificarUsuario(asociado);

			Usuario usuarioEnsesion = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
			delegado.auditarCrearUsuarioAsociado(usuarioEnsesion, usuarioAsociado);
			mostraDialogoRedirigir();

		} catch (Exception e) {
			// Exception controlada
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permitirá mostrar el dialogo de redirigir.
	 * @fecha 15/01/2018
	 */
	public void mostraDialogoRedirigir() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dialogoRedirigir').show()");
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permitirá realizar una redirección a la página de
	 *              login
	 * @return login.xthml
	 * @fecha 15/01/2018
	 */
	public String redirigirLogin() {
		return ConstantesVtrack.PAGINA_LOGIN_SIMPLE + ConstantesVtrack.REDIRECION_PAGINAS;
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permitirá realizar una redirección a la página de
	 *              perfil
	 * @return perfil.xthml
	 * @fecha 15/01/2018
	 */
	public String redirigirPerfilUsuario() {
		return ConstantesVtrack.PAGINA_PERFIL + ConstantesVtrack.REDIRECION_PAGINAS;
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Limpia todos los campos y actualiza el estado del boton crear a
	 *              deshabilitado
	 * @fecha 15/01/2018
	 */
	private void limpiarDatos() {
		this.ciudad = "";
		this.pais = "";
		this.region = "";
		this.fecha_Nacimiento = null;
		this.numeroDocumento = null;
		this.sexo = "";
		this.tipoDocumento = "";
		this.habilitarBoton = true;
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permitirá activar y desactivar el mensaje que
	 *              prioriza la necesidad del número de documento
	 * @fecha 15/01/2018
	 */
	public void activar_numero() {
		if (!this.tipoDocumento.equals(ConstantesVtrack.INVALIDO + "")) {
			if (this.numeroDocumento != null && !this.numeroDocumento.trim().equals("")) {
				this.render_tipo = false;
				this.render_numero = false;
			} else {
				this.marcaAgua_numero = "Debes ingresar el número de documento";
				this.render_tipo = true;
			}
		} else {
			if (this.numeroDocumento != null && !this.numeroDocumento.trim().equals("")) {
				this.marcaAgua_tipo = "Debes ingrese el tipo de documento";
				this.render_numero = true;
			} else {
				this.render_tipo = false;
			}
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permitirá activar y desactivar el mensaje que
	 *              prioriza la necesidad del tipo de documento
	 * @fecha 15/01/2018
	 */
	public void activar_tipo() {
		if (this.numeroDocumento != null && !this.numeroDocumento.trim().equals("")) {
			if (this.tipoDocumento != null && !this.tipoDocumento.equals(ConstantesVtrack.INVALIDO + "")) {
				this.render_numero = false;
				this.render_tipo = false;
			} else {
				this.render_numero = true;
				this.marcaAgua_tipo = "Debes ingrese el tipo de documento";
			}
		} else {
			if (!this.tipoDocumento.equals(ConstantesVtrack.INVALIDO + "")) {
				this.marcaAgua_numero = "Debes ingresar el número de documento";
				this.render_tipo = true;
			} else {
				this.render_numero = false;
			}
		}
	}

	//////////////////////////////////////////////////////////
	// Metodos Get and Set
	//////////////////////////////////////////////////////////

	public void setDelegado(DelegadoEJB delegado) {
		this.delegado = delegado;
	}

	public String getParentezco() {
		return parentezco;
	}

	public void setParentezco(String parentezco) {
		this.parentezco = parentezco;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public List<Pais> getListaPaises() {
		return listaPaises;
	}

	public void setListaPaises(List<Pais> listaPaises) {
		this.listaPaises = listaPaises;
	}

	public List<Region> getListaRegiones() {
		return listaRegiones;
	}

	public void setListaRegiones(List<Region> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}

	public List<Ciudad> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(List<Ciudad> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public List<String> getLstGeneros() {
		return lstGeneros;
	}

	public void setLstGeneros(List<String> lstGeneros) {
		this.lstGeneros = lstGeneros;
	}

	public List<TipoDocumentoPais> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	public void setListaTipoDocumento(List<TipoDocumentoPais> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	public String getTextoRegion() {
		return textoRegion;
	}

	public void setTextoRegion(String textoRegion) {
		this.textoRegion = textoRegion;
	}

	public Boolean getHabilitarBoton() {
		return habilitarBoton;
	}

	public void setHabilitarBoton(Boolean habilitarBoton) {
		this.habilitarBoton = habilitarBoton;
	}

	public Usuario getUsuarioSesion() {
		return usuarioSesion;
	}

	public void setUsuarioSesion(Usuario usuarioSesion) {
		this.usuarioSesion = usuarioSesion;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public Date getFecha_Nacimiento() {
		return fecha_Nacimiento;
	}

	public void setFecha_Nacimiento(Date fecha_Nacimiento) {
		this.fecha_Nacimiento = fecha_Nacimiento;
	}

	public Ciudad getCiudad_seleccionada() {
		return ciudad_seleccionada;
	}

	public void setCiudad_seleccionada(Ciudad ciudad_seleccionada) {
		this.ciudad_seleccionada = ciudad_seleccionada;
	}

	public Region getRegion_seleccionada() {
		return region_seleccionada;
	}

	public void setRegion_seleccionada(Region region_seleccionada) {
		this.region_seleccionada = region_seleccionada;
	}

	public Pais getPais_defecto() {
		return pais_defecto;
	}

	public void setPais_defecto(Pais pais_defecto) {
		this.pais_defecto = pais_defecto;
	}

	public Region getRegion_defecto() {
		return region_defecto;
	}

	public void setRegion_defecto(Region region_defecto) {
		this.region_defecto = region_defecto;
	}

	public Date getHoy() {
		return hoy;
	}

	public void setHoy(Date hoy) {
		this.hoy = hoy;
	}

	public String getMarcaAgua_numero() {
		return marcaAgua_numero;
	}

	public void setMarcaAgua_numero(String marcaAgua_numero) {
		this.marcaAgua_numero = marcaAgua_numero;
	}

	public String getMarcaAgua_tipo() {
		return marcaAgua_tipo;
	}

	public void setMarcaAgua_tipo(String marcaAgua_tipo) {
		this.marcaAgua_tipo = marcaAgua_tipo;
	}

	public boolean isRender_numero() {
		return render_numero;
	}

	public void setRender_numero(boolean render_numero) {
		this.render_numero = render_numero;
	}

	public boolean isRender_tipo() {
		return render_tipo;
	}

	public void setRender_tipo(boolean render_tipo) {
		this.render_tipo = render_tipo;
	}

	public boolean isRender_vista() {
		return render_vista;
	}

	public void setRender_vista(boolean render_vista) {
		this.render_vista = render_vista;
	}

}
