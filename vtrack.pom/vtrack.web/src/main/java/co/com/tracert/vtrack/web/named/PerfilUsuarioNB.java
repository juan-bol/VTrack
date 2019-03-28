package co.com.tracert.vtrack.web.named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Estado;
import co.com.tracert.vtrack.model.entities.Notificacion;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Region;
import co.com.tracert.vtrack.model.entities.TipoDocumento;
import co.com.tracert.vtrack.model.entities.TipoDocumentoPais;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class PerfilUsuarioNB extends NamedBeanUtils implements Serializable {

	//////////////////////////////////////////////////////////
	// Atributos
	//////////////////////////////////////////////////////////

	/**
	 * Serial generado
	 */
	private static final long serialVersionUID = -641098203639459628L;

	/**
	 * Delegado que se conectara con la lógica
	 */
	@EJB
	private DelegadoEJB delegado;

	/**
	 * Variable que permitirá manejar el correo electrónico del usuario
	 */
	private String correoElectronico;

	/**
	 * Variable que permitirá manejar la contraseña del correo electrónico del
	 * usuario
	 */
	private String contrasenia;

	/**
	 * Variable que permitirá manejar la contraseña del correl electrónico del
	 * usuario
	 */
	private String confirmarContrasenia;

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
	 * Variable que permitirá manejar en la vista, a través de un placeholder del
	 * tipo de documento
	 */
	private String tipoDocumento_texto;

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
	private Date fechaNacimiento;

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
	 * Variable que permitirá manejar en la vista, a través de un placeholder y
	 * outLabel la distribución geografica del pais de residencia del usuario
	 */
	private String textoRegion_combobox;

	/**
	 * Variable que permitirá manejar el usuario que se encuentra actualmente en
	 * sesión
	 */
	private Usuario usuario;

	/**
	 * Variable que permitirá bloquear el boton de crear una cuenta asociada del
	 * respectivo usuario en sesión
	 */
	private boolean bloquear_boton;

	/**
	 * Variable que permitirá bloquear la información de la cuenta del usuario en
	 * sesión
	 */
	private boolean bloquear_correo;

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
	 * Variable que permitirá manejar el SelectItem por defecto Invalido de la pais
	 * seleccionada
	 */
	private Pais pais_seleccionado;

	/**
	 * Variable que permitirá manejar el SelectItem por defecto Invalido del tipo
	 * documento seleccionado
	 */
	private TipoDocumentoPais tipoDocumentoPais_seleccionado;

	/**
	 * Variable que permitirá manejar y bloquear el nombre del usuario cuando el
	 * usuario en sesión no tiene cuenta asocida.
	 */
	private boolean inhabilitar_nombre;

	/**
	 * Variable que permitirá manejar y bloquear la fecha de nacimiento del usuario
	 * cuando el usuario en sesión no tiene cuenta asocida.
	 */
	private boolean inhabilitar_fecha;

	/**
	 * Variable que permitirá manejar y bloquear el género del usuario cuando el
	 * usuario en sesión no tiene cuenta asocida.
	 */
	private boolean inhabilitar_genero;

	/**
	 * Variable que permitirá activar las notificaciones del usuario
	 */
	private boolean activar_notificaciones;

	/**
	 * Variable que permitirá manejar el número de días con anterioridad del usuario
	 */
	private int numeroDias;

	/**
	 * Variable que permitirá inhabilitar el número de días con anterioridad del
	 * usuario
	 */
	private boolean inhabilitar_dias;

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

	/**
	 * Es la lista de usuarios a los cuales el usuario en sesión tiene permiso tanto
	 * para ver como editar su información
	 */
	private List<Permiso> listPermisos;

	/**
	 * Variable que permitirá mostrar todos los componentes de la vista. Todo dado
	 * que se debe bloquear la vista si el usuario tiene permisos de lectura y
	 * escritura
	 */
	private boolean render_vista;

	/**
	 * Variable que permitirá mostrar de que el usuario no tiene estas
	 * funcionalidades
	 */
	private boolean render_mensaje;

	/**
	 * Variable que permitirá determinar si el usuario realizo el cambio de
	 * contraseña
	 */
	private boolean cambio_contrasenia;

	//////////////////////////////////////////////////////////
	// Constructor
	//////////////////////////////////////////////////////////

	/**
	 * @descripcion Constructor de NamedBean que referencia a la vista perfil.xhtml
	 * @author Cristian Rodriguez
	 */
	public PerfilUsuarioNB() {

	}

	/**
	 * @descripcion PostConstructor de NamedBean que referencia a la vista
	 *              perfil.xhtml
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	@PostConstruct
	private void init() {
		// Obtiene el usuario en sesión, a partir del cual se obtendra la información
		// del perfil
		asignarUsuario();
		// Se inicializan las variables de control
		activarVariablesDeControl();
		try {
			// Obtener la información
			mapearInformacion();

		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
		} finally {
			// Inicializar los géneros
			inicializarGeneros();
			// Inicializar todos los paises
			inicializarPais();
			// Inicializa los tipos de documentos
			inicializarTiposDocumentos();
			// Inicializar las regiones
			iniciarlizarRegiones();
			// Inicializar las ciudades
			iniciarlizarCiudades();
		}
	}

	/**
	 * @descripcion Método que permitirá la inicializacion de las variables de
	 *              control de la vista
	 * @author Cristian Rodriguez
	 * @fecha 29/11/2018
	 */
	private void activarVariablesDeControl() {
		// Se inicializa el texto de la región.
		this.textoRegion = "Región";
		// Se inicializa el texto de la región.
		this.textoRegion_combobox = "región";
		// Variable que permitira saber si se activo los cambios en las notificaciones
		this.activar_notificaciones = false;
		// Variable que inicializa los dias de anticipación de la notificacion
		this.numeroDias = 7;
		// Variable que permitirá manejar el spinner de los dias de anticipacións
		this.inhabilitar_dias = true;
		// Se inicializa el mensaje que muestra la necesidad del número de documento
		this.marcaAgua_numero = "Ingresar número de documento";
		// Se inicializa el mensaje que muestra la necesidad del tipo de documento
		this.marcaAgua_tipo = "Ingresar tipo de documento";
		// Se inicializa los componentes de renderización del numero de documento
		this.render_numero = false;
		// Se inicializa los componentes de renderización del tipo de documento
		this.render_tipo = false;
		// Se inicializa la variable que cambio la contraseña
		this.cambio_contrasenia = false;
	}

	/**
	 * @descripcion Método que permitirá mapear la información ingresadas por el
	 *              usuario
	 * @author Cristian Rodriguez
	 * @fecha 29/11/2018
	 */
	public void mapearInformacion() {
		// Información de la cuenta
		Cuenta cuenta = this.usuario.getCuenta();
		if (cuenta == null) {
			this.correoElectronico = null;
		} else {
			this.correoElectronico = cuenta.getCorreo();
		}
		// Información del usuario
		// Mapear el campo nombre
		String nombre = this.usuario.getNombre();
		if (nombre == null) {
			this.nombre = null;
		} else {
			this.nombre = nombre;
		}
		// Mapear el campo número de documento
		String n_documento = this.usuario.getNumeroDocumento();
		if (n_documento == null) {
			this.numeroDocumento = null;
		} else {
			this.numeroDocumento = n_documento;
		}

		// Mapear el campo del género
		String genero = this.usuario.getGenero();
		if (genero == null) {
			this.sexo = null;
		} else {
			if (genero.equals(ConstantesVtrack.LETRA_MASCULINO)) {
				this.sexo = ConstantesVtrack.MASCULINO;
			} else {
				this.sexo = ConstantesVtrack.FEMENINO;
			}
		}

		// Mapear el campo la fecha de nacimiento
		Date fecha = this.usuario.getFechaNacimiento();
		if (fecha == null) {
			this.fechaNacimiento = null;
		} else {
			this.fechaNacimiento = fecha;
		}

		// Mapear el campo del tipo de documento por pais
		TipoDocumentoPais tipoDocumentoPais = this.usuario.getTipoDocumentoPai();
		if (tipoDocumentoPais == null) {
			this.tipoDocumentoPais_seleccionado = new TipoDocumentoPais();
			tipoDocumentoPais_seleccionado.setIdTipoDocPais(ConstantesVtrack.INVALIDO);
			TipoDocumento tipoDocumento = new TipoDocumento();
			this.tipoDocumentoPais_seleccionado.setTipoDocumento(tipoDocumento);
			this.tipoDocumento = ConstantesVtrack.INVALIDO + "";
		} else {
			this.tipoDocumentoPais_seleccionado = tipoDocumentoPais;
			this.tipoDocumento = tipoDocumentoPais.getTipoDocumento().getIdTipoDocumento() + "";
			this.tipoDocumento_texto = tipoDocumentoPais.getTipoDocumento().getNombre();
		}

		// Mapear el campo la ciudad
		Ciudad ciudadTemp = this.usuario.getCiudad();
		if (ciudadTemp == null) {
			this.ciudad_seleccionada = new Ciudad();
			this.ciudad_seleccionada.setNombre("Ingrese Ciudad");
			this.ciudad_seleccionada.setIdCiudad(ConstantesVtrack.INVALIDO);
			this.ciudad = ConstantesVtrack.INVALIDO + "";
		} else {
			this.ciudad_seleccionada = ciudadTemp;
			this.ciudad = ciudadTemp.getIdCiudad() + "";
		}
		// Mapear el campo la región
		Region regionTemp = this.usuario.getRegion();
		if (regionTemp == null) {
			this.region_seleccionada = new Region();
			this.region_seleccionada.setNombre("Ingrese departamento");
			this.region_seleccionada.setIdRegion(ConstantesVtrack.INVALIDO);
		} else {
			this.region_seleccionada = regionTemp;
			this.region = regionTemp.getIdRegion() + "";
		}

		// Mapear el campo del pais
		Pais paisTemp = this.usuario.getPai();
		if (paisTemp == null) {
			this.pais_seleccionado = new Pais();
			this.pais_seleccionado.setIdPais(ConstantesVtrack.INVALIDO);
			this.pais_seleccionado.setNombre("Ingrese pais");
		} else {
			this.pais_seleccionado = paisTemp;
			this.pais = paisTemp.getIdPais() + "";
		}

		// Mapear el campo del código postal
		if (this.usuario.getZipCode() == null) {
			this.codigoPostal = null;
		} else {
			this.codigoPostal = this.usuario.getZipCode();
		}

		// Obtener los campos de las notificaciones
		Notificacion notificacion = this.usuario.getNotificacion();
		if (notificacion != null) {
			this.numeroDias = notificacion.getDiasAnticipacion().intValue();
			if (notificacion.getEstado().getEstado().equals(ConstantesVtrack.ESTADO_ACTIVO)) {
				this.activar_notificaciones = true;
				this.inhabilitar_dias = false;
			} else {
				this.activar_notificaciones = false;
				this.inhabilitar_dias = true;
			}
		}

	}

	/**
	 * @descripcion Método que permitirá la inicialización de los componentes de la
	 *              lista de tipos de documentos
	 * @author Cristian Rodriguez
	 * @fecha 20/09/2018
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
	 * @fecha 20/09/2018
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
	 * @fecha 20/09/2018
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
			this.region_seleccionada.setNombre("Ingrese " + this.textoRegion_combobox);
		}
		try {
			if (this.pais_seleccionado.getIdPais() != ConstantesVtrack.INVALIDO) {
				this.textoRegion = this.pais_defecto.getDistribucionRegional();
				this.textoRegion_combobox = this.pais_defecto.getDistribucionRegional();
				this.listaRegiones = delegado.listarRegiones(this.pais_defecto);
				Collections.sort(this.listaRegiones, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
			} else {
				this.textoRegion = "Región";
				this.textoRegion_combobox = "región";
			}
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @descripcion Método que permitirá la inicialización de los componentes de de
	 *              la lista de ciudades
	 * @author Cristian Rodriguez
	 * @fecha 20/09/2018
	 */
	private void iniciarlizarCiudades() {
		// Se inicializa en null las ciudades del Sistema Vtrack
		this.listaCiudades = new ArrayList<Ciudad>();
		Long regionId = null;
		if (this.region != null) {
			regionId = Long.parseLong(this.region);
			this.region_defecto.setIdRegion(regionId);
		} else {
			this.region_defecto.setIdRegion(ConstantesVtrack.INVALIDO);
		}
		try {
			listaCiudades = delegado.listarCiudades(this.region_defecto);
			Collections.sort(this.listaCiudades, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @description Método auxilir que lista los géneros que se mostraran en el
	 *              sistema.
	 * @fecha 20/09/2018
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
	 * @description Método que permitirá realizar los cambios que se le hayan hecho
	 *              a un usuario en la vista
	 * @fecha 20/10/2018
	 */
	public void guardarCambios() {
		modificarUsuario();
	}

	/**
	 * @autor Cristian Rodríguez
	 * @description Método que permitirá realizar los cambios que el usuario efectue
	 *              sobre sus componentes
	 * @fecha 20/10/2018
	 */
	public void modificarUsuario() {
		try {
			// Modificar todos los campos del usuario
			modificarCamposUsuario();
			// Modificar las notificaciones
			modificarNotificaciones();
			// Modificar usuario
			boolean cambioContrasenia = false;
			if (this.cambio_contrasenia) {
				usuario.setCuenta(delegado.modificarCuenta(usuario.getCuenta()));
				cambioContrasenia = true;
			}

			Usuario usuarioAnterior = delegado.consultarUsuario(usuario.getIdUsuario());
			Usuario usuarioActual = this.delegado.modificarUsuario(usuario);
			this.delegado.auditarModificarPerfil(usuarioAnterior, usuarioActual,
					(Usuario) getFromSession(ConstantesVtrack.USUARIO), cambioContrasenia);
			// Actualiza la información de nuevo y muestra un mensaje indicando el éxito de
			// la operación
			// Actualizar el usuario en sesión
			Usuario usuario = this.delegado.consultarUsuario(this.usuario.getIdUsuario());
			// Se sube a la sesión el usuario
			if (usuario.getIdUsuario() == ((Usuario) getFromSession(ConstantesVtrack.USUARIO)).getIdUsuario()) {
				putInSession(ConstantesVtrack.USUARIO, usuario);
			} else {
				putInSession(ConstantesVtrack.USUARIO_IMP, usuario);
			}
			// Obtiene el usuario en sesion
			asignarUsuario();
			// Vuelve a mapear la información.
			mapearInformacion();
			addMessage(FacesMessage.SEVERITY_INFO, "Se ha modificado exitosamente el perfil");
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
			// Se realiza especificamente cuando se intente modificar los campos del usuario
			// asociado pero no se lograron realizar
			// Obtiene el usuario en sesion
			asignarUsuario();
			mapearInformacion();
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @description Método auxiliar de modificar que permitirá realizar los cambios
	 *              que el usuario efectue. En un principio, se verificará que si el
	 *              usuario es un usuario asociado los campos de Nombre, Género y
	 *              Fecha de nacimiento no pueden ser nulos
	 *
	 * @throws Exception
	 * @fecha 20/10/2018
	 */
	private void modificarCamposUsuario() throws Exception {

		Usuario usuarioImp = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
		// Si hay usuario impersonate entonces el usuario en sesión es el usuario
		// impersonate
		if (usuarioImp != null) {

			usuario = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
			String mensaje = null;
			if (this.nombre.trim().equals("")) {
				mensaje = "Para este usuario, el nombre debe ser obligatorio";
				throw new Exception(mensaje);
			} else {
				this.usuario.setNombre(this.nombre);
			}
			if (this.sexo.trim().equals(ConstantesVtrack.INVALIDO + "")) {
				mensaje = "Para este usuario, el género debe ser obligatorio";
				throw new Exception(mensaje);
			} else {
				if (this.sexo.equals(ConstantesVtrack.MASCULINO)) {
					this.usuario.setGenero(ConstantesVtrack.LETRA_MASCULINO);
				} else {
					this.usuario.setGenero(ConstantesVtrack.LETRA_FEMENINO);
				}
			}
			if (this.fechaNacimiento == null) {
				mensaje = "Para este usuario, la fecha de nacimiento debe ser obligatoria";
				throw new Exception(mensaje);
			} else {
				this.usuario.setFechaNacimiento(this.fechaNacimiento);
			}

			// Llamado al método que modificará las campos variables del usuario
			modificarCamposVariables();

		} else {

			usuario = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
			// Cuenta
			Cuenta cuenta = this.usuario.getCuenta();
			// Modifica la contraseña de la cuenta
			if (cuenta != null) {
				if (validarContraseña()) {
					cuenta.setContrasenia(this.contrasenia);
					this.cambio_contrasenia = true;
				} else {
					cuenta.setContrasenia(cuenta.getContrasenia());
					this.cambio_contrasenia = false;
				}
				this.usuario.setCuenta(cuenta);
			} else {
				this.usuario.setCuenta(null);
			}
			// Usuario

			// Modifica el nombre del usuario
			if (this.nombre != null && !this.nombre.trim().equals("")) {
				this.usuario.setNombre(this.nombre);
			} else {
				this.usuario.setNombre(null);
			}
			// Modifica el género del usuario
			if (this.sexo != null && !this.sexo.trim().equals(ConstantesVtrack.INVALIDO + "")) {
				if (this.sexo.equals(ConstantesVtrack.MASCULINO)) {
					this.usuario.setGenero(ConstantesVtrack.LETRA_MASCULINO);
				} else {
					this.usuario.setGenero(ConstantesVtrack.LETRA_FEMENINO);
				}
			} else {
				this.usuario.setGenero(null);
			}
			// Modifica el tipo de documento del usuario
			if (this.fechaNacimiento != null) {
				this.usuario.setFechaNacimiento(this.fechaNacimiento);
			} else {
				this.usuario.setFechaNacimiento(null);
			}

			// Llamado al método que modificará las campos variables del usuario
			modificarCamposVariables();
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @description Método auxiliar de modificar que permitirá realizar los cambios
	 *              que el usuario efectue, sobre los cambios realizos en sus
	 *              componentes como Nombre, Número de documento, Tipo de documento,
	 *              Género, Información de recidencia: Pais, región y ciudad. Ademas
	 *              del código postal.
	 *
	 * @throws Exception
	 * @fecha 20/10/2018
	 */
	private void modificarCamposVariables() {
		// Modifica el número de documento del usuario
		if (this.numeroDocumento != null && !this.numeroDocumento.trim().equals("")) {
			this.usuario.setNumeroDocumento(this.numeroDocumento);
		} else {
			this.usuario.setNumeroDocumento(null);
		}

		// Modifica el tipo de documento del usuario
		TipoDocumentoPais tipoDocumentoPais = this.usuario.getTipoDocumentoPai();
		Long idtipoDocumento = null;
		if (this.tipoDocumento != null && !this.tipoDocumento.equals(ConstantesVtrack.INVALIDO + "")) {
			tipoDocumentoPais = new TipoDocumentoPais();
			idtipoDocumento = Long.parseLong(this.tipoDocumento);
			tipoDocumentoPais.setIdTipoDocPais(idtipoDocumento);
			this.usuario.setTipoDocumentoPai(tipoDocumentoPais);
		} else {
			this.usuario.setTipoDocumentoPai(null);

		}

		// Modifica la ciudad del usuario
		Long idCiudad = null;
		if (this.ciudad != null && !this.ciudad.equals(ConstantesVtrack.INVALIDO + "")) {
			Ciudad ciudadTemp = new Ciudad();
			idCiudad = Long.parseLong(this.ciudad);
			ciudadTemp.setIdCiudad(idCiudad);
			this.usuario.setCiudad(ciudadTemp);
		} else {
			this.usuario.setCiudad(null);
		}

		// Modifica la region del usuario
		Long idRegion = null;
		if (this.region != null && !this.region.equals(ConstantesVtrack.INVALIDO + "")) {
			Region regionTemp = new Region();
			idRegion = Long.parseLong(this.region);
			regionTemp.setIdRegion(idRegion);
			this.usuario.setRegion(regionTemp);
		} else {
			this.usuario.setRegion(null);
		}

		// Modifica el pais del usuario
		Long idPais = null;
		if (this.pais != null && !this.pais.equals(ConstantesVtrack.INVALIDO + "")) {
			Pais paisTemp = new Pais();
			idPais = Long.parseLong(this.pais);
			paisTemp.setIdPais(idPais);
			this.usuario.setPai(paisTemp);
		} else {
			this.usuario.setPai(null);
		}

		// Modifica el código postal del usuario
		if (this.codigoPostal != null && !this.codigoPostal.trim().equals("")) {
			this.usuario.setZipCode(this.codigoPostal);
		} else {
			this.usuario.setZipCode(null);
		}
	}

	/**
	 * @throws Exception
	 * @autor Cristian Rodríguez
	 * @descripcion Valida que las contraseñas sean correctas.
	 * @fecha 2/11/2018
	 */
	private boolean validarContraseña() throws Exception {
		if (this.contrasenia.trim().equals("") || this.confirmarContrasenia.trim().equals("")) {
			return false;
		}
		if (!this.contrasenia.equals(this.confirmarContrasenia)) {
			String mensaje = "Las contraseñas no coinciden";
			throw new Exception(mensaje);
		} else if (this.contrasenia.equals(this.confirmarContrasenia)) {
			return true;
		}
		return false;
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permite la nodificación de cualquier campo de la
	 *              notificación
	 * @fecha 20/11/2018
	 */
	private void modificarNotificaciones() {

		// Se obtiene la notificación del usuario que se encuentra en sesión
		Notificacion notificacion = this.usuario.getNotificacion();
		// Se optione los días de anticipación de la notificación
		notificacion.setDiasAnticipacion(new BigDecimal(numeroDias));
		// Se optiene el estado de la notificación
		Estado estado = new Estado();
		if (this.activar_notificaciones == false) {
			estado.setEstado(ConstantesVtrack.ESTADO_INACTIVO);
		} else {
			estado.setEstado(ConstantesVtrack.ESTADO_ACTIVO);
		}

		// Realiza el cambio del estado
		notificacion.setEstado(estado);

		try {
			// Se envia la notificación con los cambios que se realizarón
			delegado.modificarNotificacion(notificacion);
			// Modificar la notificación del usuario
			this.usuario.setNotificacion(notificacion);

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
		this.mapearInformacion();
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Obtiene la región de un pais previamente seleccionado.
	 * @fecha 2/11/2018
	 */
	public void seleccionarRegion() {
		// Cambiar el tipo de documentos al pais seleccionado. Si el nombre de la región
		// cambia.
		modificarTipoDocumento();

		Pais pais = new Pais();
		if (this.pais.equals(ConstantesVtrack.INVALIDO + "")) {

			this.textoRegion = "Región";
			this.textoRegion_combobox = "región";
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
				this.textoRegion_combobox = pais.getDistribucionRegional();
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
	 * @fecha 8/10/2018
	 */
	private void modificarTipoDocumento() {
		Pais pais = new Pais();
		pais.setIdPais(ConstantesVtrack.TIPODOCUMENTO_PAIS);
		try {
			this.listaTipoDocumento = delegado.listarTipoDocumentos(pais);
			Collections.sort(this.listaTipoDocumento,
					(a, b) -> a.getTipoDocumento().getNombre().compareToIgnoreCase(b.getTipoDocumento().getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Obtiene la ciudad de una región previamente seleccionada
	 * @fecha 8/10/2018
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
	 * @author Steven M
	 * @descripcion asigna a la var usuario el valor: 1. Correspondiente con el
	 *              usuario que se logeo 2. Correspondiente con el usuario
	 *              impersonate
	 * @fecha 1/11/2018
	 */
	public void asignarUsuario() {
		Usuario usuarioImp = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
		// Si hay usuario impersonate entonces el usuario en sesión es el usuario
		// impersonate
		if (usuarioImp != null) {

			try {
				usuario = (Usuario) getFromSession(ConstantesVtrack.USUARIO_IMP);
				usuario = this.delegado.consultarUsuario(this.usuario.getIdUsuario());

				Usuario usuarioLogeado = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
				this.bloquear_boton = false;
				this.bloquear_correo = true;
				this.listPermisos = delegado.darPermisosDestinoDeAprobado(usuarioLogeado);
				// Verificar que tipo de permisos tiene el usuario que se encuentra en
				// reemplazo. Si el tipo de permiso es Lectura y Escritura o de Lectura no
				// mostrará la vista del perfil de usuario
				Permiso permiso = obtenerPermisoSeleccionado(this.usuario.getIdUsuario());
				if (permiso.getTipoPermiso().equals(ConstantesVtrack.PERMISO_LECTURA)
						|| permiso.getTipoPermiso().equals(ConstantesVtrack.PERMISO_ESCRITURA)) {
					this.render_vista = false;
					this.render_mensaje = true;
				} else {
					this.render_vista = true;
					this.render_mensaje = false;
				}

			} catch (Exception e) {
				addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
			}
		} else {
			try {
				usuario = (Usuario) getFromSession(ConstantesVtrack.USUARIO);
				usuario = this.delegado.consultarUsuario(this.usuario.getIdUsuario());
			} catch (Exception e) {
				addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
			}
			this.bloquear_boton = true;
			this.bloquear_correo = true;
			this.render_vista = true;
			this.render_mensaje = false;
		}
	}

	/**
	 * @author Steven M
	 * @descrpcion Busca el permiso que se selecciono en la lista de select one menu
	 *             a partir de su id
	 * @param id del usuario que se busca en la lista del NB permisos
	 * @return Permiso
	 * @fecha 4/11/2018
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
	 * @descripcion Borra de la lista de permisos el usuario al que le corresponda
	 *              ese id. Evita la repetición en la lista de permisos del select
	 *              one menu
	 * @param id del usuario al que se quiere borra de las lista de permisos
	 * @fecha 4/11/2018
	 */
	public void borrarPermisoRepetido(Long id) {
		boolean encontro = false;
		for (int i = 0; i < listPermisos.size() && !encontro; i++) {
			if (listPermisos.get(i).getUsuarioDestino().getIdUsuario() == id) {
				listPermisos.remove(i);
				encontro = true;
			}
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permitirá activar y desactivar el mensaje que
	 *              prioriza la necesidad del número de documento
	 * @fecha 10/11/2018
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
	 * @fecha 10/11/2018
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

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permitirá activar y desactivar las notificaciones por
	 *              correo de los recordatorios
	 * @fecha 10/10/2018
	 */
	public void notificarCambio() {
		if (this.activar_notificaciones == false) {
			this.activar_notificaciones = true;
			this.inhabilitar_dias = true;
			addMessage(FacesMessage.SEVERITY_INFO, "Desactivarás el servicio de notificaciones vía correo");

		} else {
			this.activar_notificaciones = false;
			this.inhabilitar_dias = false;
			addMessage(FacesMessage.SEVERITY_INFO, "Activarás el servicio de notificaciones vía correo");

		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Método que permitirá realizar una redirección a la página de
	 *              cuenta asociada
	 * @return ./paginas/crearCuentaAsociada.xthml
	 * @fecha 10/10/2018
	 */
	public String redirigirCuentaAsociada() {
		return "/paginas/crearCuentaAsociada.xhtml" + ConstantesVtrack.REDIRECION_PAGINAS;
	}

	/**
	 * 
	 */
	public void metodoAlerta() {
		String mensaje = "Este método solo es para evitar un refresh automatico";
	}

	//////////////////////////////////////////////////////////
	// Metodos Get and Set
	//////////////////////////////////////////////////////////

	public DelegadoEJB getDelegado() {
		return delegado;
	}

	public void setDelegado(DelegadoEJB delegado) {
		this.delegado = delegado;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String getConfirmarContrasenia() {
		return confirmarContrasenia;
	}

	public void setConfirmarContrasenia(String confirmarContrasenia) {
		this.confirmarContrasenia = confirmarContrasenia;
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

	public List<String> getLstGeneros() {
		return lstGeneros;
	}

	public void setLstGeneros(List<String> lstGeneros) {
		this.lstGeneros = lstGeneros;
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

	public Usuario getusuario() {
		return usuario;
	}

	public void setusuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isBloquear_boton() {
		return bloquear_boton;
	}

	public void setBloquear_boton(boolean bloquear_boton) {
		this.bloquear_boton = bloquear_boton;
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

	public Pais getPais_seleccionado() {
		return pais_seleccionado;
	}

	public void setPais_seleccionado(Pais pais_seleccionado) {
		this.pais_seleccionado = pais_seleccionado;
	}

	public TipoDocumentoPais getTipoDocumentoPais_seleccionado() {
		return tipoDocumentoPais_seleccionado;
	}

	public void setTipoDocumentoPais_seleccionado(TipoDocumentoPais tipoDocumentoPais_seleccionado) {
		this.tipoDocumentoPais_seleccionado = tipoDocumentoPais_seleccionado;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getTipoDocumento_texto() {
		return tipoDocumento_texto;
	}

	public void setTipoDocumento_texto(String tipoDocumento_texto) {
		this.tipoDocumento_texto = tipoDocumento_texto;
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

	public boolean isInhabilitar_nombre() {
		return inhabilitar_nombre;
	}

	public void setInhabilitar_nombre(boolean inhabilitar_nombre) {
		this.inhabilitar_nombre = inhabilitar_nombre;
	}

	public boolean isInhabilitar_fecha() {
		return inhabilitar_fecha;
	}

	public void setInhabilitar_fecha(boolean inhabilitar_fecha) {
		this.inhabilitar_fecha = inhabilitar_fecha;
	}

	public boolean isInhabilitar_genero() {
		return inhabilitar_genero;
	}

	public void setInhabilitar_genero(boolean inhabilitar_genero) {
		this.inhabilitar_genero = inhabilitar_genero;
	}

	public boolean isBloquear_correo() {
		return bloquear_correo;
	}

	public void setBloquear_correo(boolean bloquear_correo) {
		this.bloquear_correo = bloquear_correo;
	}

	public boolean isActivar_notificaciones() {
		return activar_notificaciones;
	}

	public void setActivar_notificaciones(boolean activar_notificaciones) {
		this.activar_notificaciones = activar_notificaciones;
	}

	public int getNumeroDias() {
		return numeroDias;
	}

	public void setNumeroDias(int numeroDias) {
		this.numeroDias = numeroDias;
	}

	public boolean isInhabilitar_dias() {
		return inhabilitar_dias;
	}

	public void setInhabilitar_dias(boolean inhabilitar_dias) {
		this.inhabilitar_dias = inhabilitar_dias;
	}

	public Date getHoy() {
		Calendar c = Calendar.getInstance();
		return c.getTime();
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

	public List<Permiso> getListPermisos() {
		return listPermisos;
	}

	public void setListPermisos(List<Permiso> listPermisos) {
		this.listPermisos = listPermisos;
	}

	public boolean isRender_vista() {
		return render_vista;
	}

	public void setRender_vista(boolean render_vista) {
		this.render_vista = render_vista;
	}

	public boolean isRender_mensaje() {
		return render_mensaje;
	}

	public void setRender_mensaje(boolean render_mensaje) {
		this.render_mensaje = render_mensaje;
	}

	public String getTextoRegion_combobox() {
		return textoRegion_combobox;
	}

	public void setTextoRegion_combobox(String textoRegion_combobox) {
		this.textoRegion_combobox = textoRegion_combobox;
	}

	public boolean isCambio_contrasenia() {
		return cambio_contrasenia;
	}

	public void setCambio_contrasenia(boolean cambio_contrasenia) {
		this.cambio_contrasenia = cambio_contrasenia;
	}

}
