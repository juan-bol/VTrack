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

import org.primefaces.PrimeFaces;

import co.com.tracert.vtrack.model.constants.ConstantesVtrack;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Notificacion;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Region;
import co.com.tracert.vtrack.model.entities.TipoDocumentoPais;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.exceptions.ExcepcionNegocio;
import co.com.tracert.vtrack.web.businessdelegate.DelegadoEJB;
import co.com.tracert.vtrack.web.utils.NamedBeanUtils;

@ViewScoped
@Named
public class CrearCuentaUsuarioNB extends NamedBeanUtils implements Serializable {

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
	 * Variable que permitirá manejar la región de residencia del usuario
	 */
	private String region;

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
	 * Variable que permitirá ingresar el nombre del tipo de región
	 */
	private String textoCombobox_region;

	/**
	 * Variable que permitirá activar la creación del usuario
	 */
	private Boolean habilitarBoton;

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

	//////////////////////////////////////////////////////////
	// Constructor
	//////////////////////////////////////////////////////////

	/**
	 * @descripcion Constructor de NamedBean que referencia a la vista
	 *              crearCuentaUsuario.xhtml
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	public CrearCuentaUsuarioNB() {

	}

	/**
	 * @descripcion PostConstructor de NamedBean que referencia a la vista
	 *              CrearCuentaUsuario.xhtml
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	@PostConstruct
	private void init() {
		// Se inicializan las variables de control
		activarVariablesDeControl();
		try {
			// Inicializa la lista de los paises en el sistema Vtrack
			// Inicializar todos los paises
			inicializarPais();
			// Inicializa los tipos de documentos
			inicializarTiposDocumentos();
			// Método para inicializar los géneros
			inicializarGeneros();
		} catch (Exception e) {
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
		// Se inicializa el label del Combox de acuerdo a la rdistribución del pais.
		this.textoCombobox_region = "Ingresar región";
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
	 * @descripcion Método que permitirá la inicialización de los componentes de la
	 *              lista de tipos de documentos
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	private void inicializarTiposDocumentos() {
		// inicializar tipo de documentos
		// se inicializa los tipos de documento dado el pais. Por defecto sera
		// Colombia
		Pais pais = new Pais();
		pais.setIdPais(ConstantesVtrack.TIPODOCUMENTO_PAIS);
		// Inicializa la lista de tipos documentos del pais por defecto Colombia
		try {
			this.listaTipoDocumento = delegado.listarTipoDocumentos(pais);
			Collections.sort(this.listaTipoDocumento,
					(a, b) -> a.getTipoDocumento().getNombre().compareToIgnoreCase(b.getTipoDocumento().getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
		}

	}

	/**
	 * @descripcion Método que permitirá la inicialización de los componentes de de
	 *              la lista de paises
	 * @author Cristian Rodriguez
	 * @fecha 15/01/2018
	 */
	private void inicializarPais() {
		// Para esta versión solo aparecerán Colombia
		Pais pais_defecto;
		try {
			pais_defecto = delegado.obtenerPais(ConstantesVtrack.PAIS_DEFECTO);
			this.listaPaises = new ArrayList<Pais>();
			this.listaPaises.add(pais_defecto);
			Collections.sort(this.listaPaises, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_INFO, e.getMessage());
		}

	}

	/**
	 * @autor Cristian Rodríguez
	 * @description Método auxilir que lista los géneros que se mostraran en el
	 *              sistema.
	 * @fecha 15/01/2018
	 */
	private void inicializarGeneros() {
		// Inicializa la lista de los generos en el sistema Vtrack
		this.lstGeneros = new ArrayList<String>();
		this.lstGeneros.add(ConstantesVtrack.MASCULINO);
		this.lstGeneros.add(ConstantesVtrack.FEMENINO);
		Collections.sort(this.lstGeneros, (a, b) -> a.compareToIgnoreCase(b));
	}

	/**
	 * @autor Cristian Rodríguez
	 * @description Metodo que realiza la validación de la nulidad en los campos
	 *              obligatorios para habilitar el boton de crear usuario
	 * @fecha 15/01/2018
	 */
	public void habilitarCrearCuenta() {
		if (this.correoElectronico != null && !this.correoElectronico.equals("") && this.contrasenia != null
				&& !this.contrasenia.equals("") && this.confirmarContrasenia != null
				&& !this.confirmarContrasenia.equals("")) {
			this.habilitarBoton = false;
		} else {
			this.habilitarBoton = true;
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
		// Se toma el identificador del pais para confirmar la región a seleccionar
		Long paisId = Long.parseLong(this.pais);
		pais.setIdPais(paisId);
		try {
			// Modificar la distribución del pais seleccionado
			pais = delegado.obtenerPais(paisId);
			this.textoRegion = pais.getDistribucionRegional() + ":";
			this.textoCombobox_region = "Ingresar " + pais.getDistribucionRegional();
			listaRegiones = delegado.listarRegiones(pais);
			Collections.sort(listaRegiones, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
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
	 * @descripcion Obtiene la ciudad de una región previamente seleccionada
	 * @fecha 15/01/2018
	 */
	public void seleccionarCiudad() {
		Region region = new Region();
		// Se toma el identificador de la región para confirmar la ciudad a seleccionar
		Long regionId = Long.parseLong(this.region);
		region.setIdRegion(regionId);
		try {
			this.listaCiudades = delegado.listarCiudades(region);
			Collections.sort(this.listaCiudades, (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
		} catch (Exception e) {
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Realiza el registro de la información ingresada. Se crea una
	 *              Cuanta dado por la información expres y si se ingreso datos del
	 *              usuario lo asocia a esta cuenta.
	 * @fecha 15/01/2018
	 * 
	 */
	public void realizarRegistro() {
		// Valida que las constraseñas sean iguales
		if (validarContraseña()) {
			// Envia la información del registro para las validaciones de las mismas
			registrarCuenta();
		}
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Valida que las contraseñas sean validas
	 * @fecha 15/01/2018
	 * 
	 */
	private boolean validarContraseña() {
		boolean agregar = false;
		if (this.contrasenia != null && this.confirmarContrasenia != null
				&& !contrasenia.equals(confirmarContrasenia)) {
			String mensaje = "Las contraseñas no coinciden";
			addMessage(FacesMessage.SEVERITY_ERROR, mensaje);
		}
		if (this.contrasenia != null && this.confirmarContrasenia != null && contrasenia.equals(confirmarContrasenia)) {
			agregar = true;
		}
		return agregar;
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Realiza el mapeo de la información ingresada para crear una
	 *              cuenta y su respectivo usuario asociado.
	 * @fecha 15/01/2018
	 */
	private void registrarCuenta() {
		try {
			// Instancia la cuenta
			Cuenta cuenta = new Cuenta();
			// Obtener la información ingresado por el cliente del sistema.
			cuenta.setCorreo(this.correoElectronico);
			cuenta.setContrasenia(this.contrasenia);

			// Instancia el usuario
			Usuario usuario = new Usuario();

			// Obtener la información ingresado por el cliente del sistema.
			usuario.setNombre(this.nombre);
			usuario.setFechaNacimiento(this.fechaNacimiento);
			usuario.setZipCode(this.codigoPostal);
			usuario.setNumeroDocumento(this.numeroDocumento);

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

			// Validar el ingreso de Tipo de documento y de la cédula.
			if (this.tipoDocumento != null && !this.tipoDocumento.equals(ConstantesVtrack.INVALIDO + "")) {
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

			if (this.ciudad != null) {
				idCiudad = Long.parseLong(this.ciudad);
				ciudad.setIdCiudad(idCiudad);
				usuario.setCiudad(ciudad);
			} else {
				usuario.setCiudad(null);
			}

			// Se busca que si el cliente no ingreso algun dato. Se envia como elemento nulo
			// del pais.
			Pais pais = new Pais();
			Long idPais = null;

			if (this.pais != null) {
				idPais = Long.parseLong(this.pais);
				pais.setIdPais(idPais);
				usuario.setPai(pais);
			} else {
				usuario.setPai(null);
			}

			// Se busca que si el cliente no ingreso algun dato. Se envia como elemento nulo
			// de la region.
			Region region = new Region();
			Long idRegion = null;

			if (this.region != null) {
				idRegion = Long.parseLong(this.region);
				region.setIdRegion(idRegion);
				usuario.setRegion(region);
			} else {
				usuario.setRegion(null);
			}

			// Validar que la contrasenia no contenga el numero de identificación
			if (usuario.getNumeroDocumento() != null && !usuario.getNumeroDocumento().trim().equals("")
					&& cuenta.getContrasenia() != null
					&& cuenta.getContrasenia().contains(usuario.getNumeroDocumento()))
				throw new ExcepcionNegocio("La contraseña no puede contener el número de identificación");

			// primero creo la cuenta y el usuario
			usuario = delegado.crearCuentaUsuario(usuario, cuenta);
			// creamos la notificacion
			Notificacion notificacion = new Notificacion();
			notificacion.setDiasAnticipacion(new BigDecimal(ConstantesVtrack.NOTIFICAR_DIAS_ANTICIPACION));
			notificacion.setUsuario(usuario);
			notificacion = delegado.crearNotificacion(notificacion);

			// vinculamos la cuenta y el usuario y la notificacion
			cuenta = usuario.getCuenta();
			usuario.setNotificacion(notificacion);
			usuario = delegado.vincularCuentaUsuario(cuenta, usuario);
			delegado.auditarRegistro(usuario);

			// Inicia en sesión al usuario recientemente creado
			iniciarSesion(usuario);
			delegado.auditarAutenticacion(null, usuario.getCuenta());
			// Mostrar el dialogo de registro correcto
			mostrarDialogoRegistro();
			// Limpia todos los campos despues del registro
			limpiarDatos();
		} catch (Exception e) {
			habilitarCrearCuenta();
			addMessage(FacesMessage.SEVERITY_ERROR, e.getMessage());
		}
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

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Mostrara el dialogo que confirma la creación satisfatoria de un
	 *              usuario en el sistema.
	 * @fecha 15/01/2018
	 */
	public void mostrarDialogoRegistro() {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('dlg_confirmar').show();");
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Se encarga de poner en sesión al usuario que se creo
	 * @param usuario
	 * @throws Exception
	 * @fecha 15/01/2018
	 */
	private void iniciarSesion(Usuario usuario) {
		putInSession(ConstantesVtrack.USUARIO, usuario);

	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Limpia todos los campos y actualiza el estado del boton crear a
	 *              deshabilitado
	 * @fecha 15/01/2018
	 */
	private void limpiarDatos() {
		this.nombre = "";
		this.ciudad = "";
		this.contrasenia = "";
		this.confirmarContrasenia = "";
		this.correoElectronico = "";
		this.pais = "";
		this.region = "";
		this.fechaNacimiento = null;
		this.numeroDocumento = "";
		this.sexo = "";
		this.tipoDocumento = "";
		this.codigoPostal = "";
		this.habilitarBoton = true;
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Regresa a la pagina de login dado que se cancelo el registro
	 * @return login.xhtml
	 * @fecha 15/01/2018
	 */
	public String redirigirLogin() {
		return "index.xhtml" + ConstantesVtrack.REDIRECION_PAGINAS;
	}

	/**
	 * @autor Cristian Rodríguez
	 * @descripcion Redirige a la pagina de esquemas sugeridos
	 * @return gestionEsquemasSugeridos.xhtml
	 * @fecha 15/01/2018
	 */
	public String redirigirPaginaEsquemasSugeridos() {
		return ConstantesVtrack.PAGINA_INICIO + ConstantesVtrack.REDIRECION_PAGINAS;
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

	public Boolean getHabilitarBoton() {
		return habilitarBoton;
	}

	public void setHabilitarBoton(Boolean bandera) {
		this.habilitarBoton = bandera;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getTextoCombobox_region() {
		return textoCombobox_region;
	}

	public void setTextoCombobox_region(String textoCombobox_region) {
		this.textoCombobox_region = textoCombobox_region;
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

}
