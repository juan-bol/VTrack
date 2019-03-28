package co.com.tracert.vtrack.web.businessdelegate;

import java.util.Date;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import co.com.tracert.vtrack.model.dto.EsquemaDTO;
import co.com.tracert.vtrack.model.entities.CentroVacunacion;
import co.com.tracert.vtrack.model.entities.Ciudad;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Dosis;
import co.com.tracert.vtrack.model.entities.DosisAplicada;
import co.com.tracert.vtrack.model.entities.Esquema;
import co.com.tracert.vtrack.model.entities.EsquemaAgregado;
import co.com.tracert.vtrack.model.entities.Menu;
import co.com.tracert.vtrack.model.entities.Notificacion;
import co.com.tracert.vtrack.model.entities.Pais;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Region;
import co.com.tracert.vtrack.model.entities.TipoDocumentoPais;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.entities.Vacuna;
import co.com.tracert.vtrack.model.entities.VacunaOculta;
import co.com.tracert.vtrack.model.interfaces.IControlCarneVacunacionRemota;
import co.com.tracert.vtrack.model.interfaces.IControlCentroVacunacionRemota;
import co.com.tracert.vtrack.model.interfaces.IControlEsquemaVacunacionRemota;
import co.com.tracert.vtrack.model.interfaces.IInformacionGeograficaRemota;
import co.com.tracert.vtrack.model.interfaces.IInformacionUsuarioRemota;
import co.com.tracert.vtrack.model.interfaces.INotificacionesRemota;

/*he comentado esta  linea para establecer stateful y que me garantize el estado de la sesión*/
@Stateless(name = "DelegadoEJB")
public class DelegadoEJB {

	

	@EJB(lookup = "java:global/vtrack/vtrack.logic/ControlEsquemaVacunacionBean!co.com.tracert.vtrack.model.interfaces.IControlEsquemaVacunacionRemota")
	private IControlEsquemaVacunacionRemota iControlEsquemaVacunacionRemota;

	@EJB(lookup = "java:global/vtrack/vtrack.logic/InformacionUsuarioBean!co.com.tracert.vtrack.model.interfaces.IInformacionUsuarioRemota")
	private IInformacionUsuarioRemota iInformacionUsuarioRemota;

	@EJB(lookup = "java:global/vtrack/vtrack.logic/ControlCarneVacunacionBean!co.com.tracert.vtrack.model.interfaces.IControlCarneVacunacionRemota")
	private IControlCarneVacunacionRemota iControlCarneVacunacionRemota;

	@EJB(lookup = "java:global/vtrack/vtrack.logic/InformacionGeograficaBean!co.com.tracert.vtrack.model.interfaces.IInformacionGeograficaRemota")
	private IInformacionGeograficaRemota iInformacionGeograficaRemota;

	@EJB(lookup = "java:global/vtrack/vtrack.logic/NotificacionesBean!co.com.tracert.vtrack.model.interfaces.INotificacionesRemota")
	private INotificacionesRemota iNotificacionesRemota;
	
	@EJB(lookup = "java:global/vtrack/vtrack.logic/ControlCentrosVacunacionBean!co.com.tracert.vtrack.model.interfaces.IControlCentroVacunacionRemota")
	private IControlCentroVacunacionRemota iCentrosVacunacionRemota;

	/**
	 * @descripcion sugiere esquemas al usuario que se ingresa como parametro
	 * @author Diego Lamus
	 * @param usuario usuario al cual se van a sugerir esquemas de vacunacion
	 * @return lista de esuqemas sugeridos
	 * @throws Exception
	 */
	public List<EsquemaDTO> sugerirEsquemasVacunacion(Usuario usuario, Pais pais) throws Exception {
		return iControlEsquemaVacunacionRemota.sugerirEsquemasVacunacion(usuario, pais);
	}

	/**
	 * @author Diego Lamus
	 * @Descripcion encuentra un usuario por su id
	 * @param id id del usuario que se quiere consultar en la base de datos
	 * @return usuario - instancia de usuario en la base de datos
	 * @throws Exception si el id del usuario no es valida o se genero algun error
	 *                   en la busqueda del usuario
	 */
	public Usuario consultarUsuario(Long id) throws Exception {
		return iInformacionUsuarioRemota.consultarUsuario(id);
	}

	/**
	 * @descripcion encuentra los menus disponibles para el usuario
	 * @param objeto usuario que se quiere consultar en la base de datos
	 * @return Liestado de menus corrspondientes al usuario
	 * @throws Exception si el
	 */
	public List<Menu> consultarMenuUsuario(Usuario usuario) throws Exception {
		return iInformacionUsuarioRemota.consultarMenuUsuario(usuario);
	}

	/**
	 * @descripcion Agrega un esquema sugerido al usuario
	 * @author Diego Lamus
	 * @fecha 8/10/2018
	 * @param usuario usuario al cual se le agregara el esquema
	 * @param esquema esquema que se agregara al carne de vacunacion del usuario
	 * @return EsquemaAgregado al carne
	 */
	public EsquemaAgregado agregarEsquemaSugerido(Usuario usuario, Esquema esquema) throws Exception {
		return iControlEsquemaVacunacionRemota.agregarEsquemaSugerido(usuario, esquema);
	}

	/**
	 * @descripcion Calcula la cantidad de dosis maxima de las vacunas de un esquema
	 * @author Juan Bolanios
	 * @param esquema
	 * @return numero de dosis maxima de las vacunas de un esquema
	 */
	public int darDosisMaxima(Esquema esquema) throws Exception {
		return iControlEsquemaVacunacionRemota.darDosisMaxima(esquema);
	}

	/**
	 * @descripcion Registra la aplicacion de una dosis en un esquema del carne del
	 *              usuario
	 * @author Juan Bolanios
	 * @param usuario Usuario que aplicara la dosis
	 * @param esquema Dosis que se aplicara el usuario
	 */
	public void registrarAplicacionDosis(Usuario usuario, Dosis dosis, Date fecha) throws Exception {
		iControlCarneVacunacionRemota.registrarAplicacionDosis(usuario, dosis, fecha);
	}

	/**
	 * @descripcion modifica la dosis aplicada en los esquemas del carne de
	 *              vacunacion del usuario
	 * @author Diego Lamus, Valery Ibarra
	 * @fecha 23/11/2018
	 * @param dosisAplicada Es la dosis que un usuario tiene aplicada en su carne y
	 *                      desea eliminar dicha aplicacion
	 * @throws Exception si la dosis aplicada es nula o si hay problemas en el dao
	 */
	public DosisAplicada modificarAplicacionDosis(DosisAplicada dosisAplicada, String estado) throws Exception {
		return iControlCarneVacunacionRemota.modificarAplicacionDosis(dosisAplicada,estado);
	}

	/**
	 * @descripcion Ocultar la vista de una vacuna en el carne de vacunacion
	 * @author Valery Ibarra
	 * @fecha 10/10/2018
	 * @param usuario Usuario que ocultara la vacuna en su carne
	 * @param vacuna  Vacuna que se ocultara para todos los esquemas del carne del
	 *                usuario
	 * @throws Exception si el usuario o vacunas ingresados son nulos, tambien
	 *                   cuando hay error en las consultas a la base de datos
	 */
	public VacunaOculta ocultarVacunaCarne(Usuario usuario, Vacuna vacuna) throws Exception {
		return iControlCarneVacunacionRemota.ocultarVacunaCarne(usuario, vacuna);
	}
	

	/**
	 * @descripcion devuelve el carne de vacunacion de un usuario
	 * @author Diego Lamus
	 * @fecha 9/10/2018
	 * @param usuario usuario sobre el cual se consultara el carne de vacunacion
	 * @return carne vacunacion
	 * @throws Exception si el usuario ingresado es nulo o sucede algun error en la
	 *                   consulta a la base de datos
	 */
	public List<EsquemaDTO> darCarneVacunacion(Usuario usuario) throws Exception {
		return iControlCarneVacunacionRemota.darCarneVacunacion(usuario);
	}

	/**
	 * @descripcion devuelve la lista de los paises del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista de paises del sistema
	 * @throws Exception
	 */
	public List<Pais> listarPaises() throws Exception {
		return iInformacionGeograficaRemota.listarPaises();
	}
	
	
	/**
	 * @descripcion devuelve la lista de los regiones dado un determinado pais del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista de regiones pertenecientes a un pais
	 * @throws Exception
	 */
	public List<Region> listarRegiones(Pais pais) throws Exception {
		return iInformacionGeograficaRemota.listarRegiones(pais);
	}
	
	
	/**
	 * @descripcion devuelve la lista de las ciudades dado una determinada región del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista de las ciudades pertenecientes a una region
	 * @throws Exception
	 */
	public List<Ciudad> listarCiudades(Region region) throws Exception {
		return iInformacionGeograficaRemota.listarCiudades(region);
	}
	
	/**@author Juan Bolaños
	 * @description Permitir obtener la distribución politica de cada pais en el sistema Vtrack
	 * @param idPais Identificador del pais
	 * @throws Exception
	 */
	public Pais obtenerPais(Long idPais) throws Exception {
		return iInformacionGeograficaRemota.obtenerPais(idPais);
	}

	/**
	 * @descripcion devuelve la lista de los Tipo de documentos de un determinado pais del sistema Vtrack
	 * @author Juan Bolaños
	 * @return Lista los tipos de documento pertenecientes a un pais
	 * @throws Exception
	 */
	public List<TipoDocumentoPais> listarTipoDocumentos(Pais pais) throws Exception {
		return iInformacionGeograficaRemota.listarTipoDocumentos(pais);
	}

	/**
	 * @descripcion registra una cuenta al sistema Vtrack. Se realiza a partir de
	 *              ingresar solo el correo y la contraseña
	 * @throws Exception
	 */
	public Cuenta crearCuenta(Cuenta cuenta) throws Exception {
		return iInformacionUsuarioRemota.crearCuenta(cuenta);
	}

	/**
	 * @author Juan Bolaños - Valery Ibarra
	 * @description Se registra un usuario
	 * @param usuario que se desea crear
	 * @throws Exception
	 */
	public Usuario crearUsuario(Usuario usuario) throws Exception {
		return iInformacionUsuarioRemota.crearUsuario(usuario);
	}

	
	/**
	 * @author Diego lamus
	 * @descripción se deberá traer el usuario a traves de la cuenta basado en su correo y contraseña recibidos por parametro.
	 * @returns Cuenta cuenta del usuario que hace inicio de sesion
	 * @throws Exception En caso que no se encuentre se al usuario o su contraseña no sea valida
	 * @fecha 27/10/2018
	 */
	public Usuario iniciarSesion(Cuenta cuentaUsuario) throws Exception {
		return iInformacionUsuarioRemota.iniciarSesion(cuentaUsuario);
	}
	

	/**
	 * @descripcion Devuelve los permisos que tengo sobre otros usuarios
	 * @author Valery Ibarra
	 * @fecha 1/11/2018
	 * @param usuarioOrigen Usuario que tiene permisos y se desea saber sobre cuales
	 *                      usuarios tiene dicho permiso
	 * @returns Lista de permisos que tiene el usuario
	 * @throws Exception Si no encuentra el usuario ingresado en los permisos
	 */
	public List<Permiso> darPermisosDestinoDeAprobado(Usuario usuarioOrigen) throws Exception {
		return iInformacionUsuarioRemota.darPermisosDestinoDeAprobado(usuarioOrigen);

	}
	
	/**
	 * @descripcion Modifica la cuenta
	 * @author Victor Potes
	 * @fecha 3/11/2018
	 * @param cuenta cuenta que se va a modificar
	 * @throws Exception Pueden salir excepciones de reglas de negocio
	 */
	public Cuenta modificarCuenta (Cuenta cuenta) throws Exception{
		return iInformacionUsuarioRemota.modificarCuenta(cuenta);
	}
	
	
	/**
	 * @descripcion Modifica el usuario ingresado
	 * @author Valery Ibarra
	 * @fecha 3/11/2018
	 * @param usuario Usuario que se va a modificar
	 * @returns El usuario modificado
	 * @throws Exception Excepciones de reglas de negocio
	 */
	public Usuario modificarUsuario(Usuario usuario) throws Exception {
		return iInformacionUsuarioRemota.modificarUsuario(usuario);
	}
	
	/** 
     * @descripcion Cambia en usuario su cuenta y cambia en cuenta su usuario
	 * @author Valery Ibarra
	 * @fecha 6/11/2018
	 * @param cuenta que se va a vincular, es decir, que se le va asignar su usuario
	 * @param usuario que se va a vincular, es decir, que se le va asignar su cuenta
	 * @returns usuario que ya esta vinculado
	 * @throws Exception Excepciones de reglas de negocio
	 */
	public Usuario vincularCuentaUsuario(Cuenta cuenta, Usuario usuario) throws Exception {
		return iInformacionUsuarioRemota.vincularCuentaUsuario(cuenta, usuario);
	}
	
	/**
	 * @descripcion Este metodo determina si un esquema que el usuario tiene agregado a su carne no tiene vacunas, si es así, lo elimina
	 * @author Juan Bolaños
	 * @param esquema del usuario
	 * @throws Exception
	 */
	public void verificarEsquemaAgregadoVacio(Usuario usuario, Esquema esquema) throws Exception {
		// TODO Agregar al deployment
		iControlCarneVacunacionRemota.verificarEsquemaAgregadoVacio(usuario, esquema);
	}
	
	/**
	 * @author Victor Potes
	 * @descripción Busca la cuenta de acuerdo a su correo
	 * @parametro correo correo de la cuenta que se piensa retornar
	 * @returns Cuenta cuenta que tiene el correo ingresado por parametro
	 * @throws Exception En caso que no se encuentre encuentre la cuenta, significa
	 *                   que aun no se ha registrado con ese correo
	 * @fecha 10/11/2018
	 */
	public Cuenta encontrarCuentaPorCorreo (String correo) throws Exception{
		//TODO Agregar encontrarCuentaPorCorreo al deployment
		return iInformacionUsuarioRemota.encontrarCuentaPorCorreo(correo);
	}
	
	/**
	 * @author Victor Potes
	 * @descripción Busca la ciudad por su id
	 * @parametro idCiudad id de la ciudad
	 * @returns Ciudad ciudad perteneciente al id ingresado por parametro
	 * @throws Exception En caso que no se encuentre encuentre la ciudad
	 * @fecha 10/11/2018
	 */
	public Ciudad encontrarCiudad (Long idCiudad) throws Exception{
		//TODO Agregar encontrarCiudad al deployment
		return iInformacionGeograficaRemota.obtenerCiudad(idCiudad);
	}
	
	
	/**
	 * @author Diego Lamos
	 * @descripción Modifica la configuracion de notificaciones del usuario
	 * @parametro Notificacion
	 * @returns Notificación modificada
	 * @throws Exception En caso de que no sea posible modificar la notificacion 
	 * @fecha 17/11/2018
	 */
	public void modificarNotificacion(Notificacion notificacion) throws Exception {
		// TODO agregar al deployment
		iInformacionUsuarioRemota.modificarNotificacion(notificacion);	
	}
	
	/**
	 * @author Diego Lamos
	 * @descripción crea la configuracion de notificaciones del usuario
	 * @parametro Notificacion
	 * @returns Notificación creada
	 * @throws Exception En caso de que no sea posible crear la notificacion 
	 * @fecha 17/11/2018
	 */
	public Notificacion  crearNotificacion(Notificacion notificacion) throws Exception {
		// TODO agregar al deployment
		return iInformacionUsuarioRemota.crearNotificacion(notificacion);	
	}
	
	/**
	 * @descripcion Configura un permiso dependiendo las caracteristicas del mismo en su creacion y modificacion
	 * @author Valery Ibarra
	 * @fecha 19/11/2018
	 * @param permiso Permiso con la informacion a modificar o crear en la base de datos
	 * @returns Permiso configurado
	 * @throws Exception si los usuarios ingresados son nulos o no cumplen lo
	 *                   estipulado, asimismo con el nombre de cuenta o sucede algun
	 *                   error en la consulta a la base de datos
	 */
	public Permiso configurarPermisoGeneral(Permiso permiso) throws Exception {
		return iInformacionUsuarioRemota.configurarPermisoGeneral(permiso);
	}
	
	/**
	 * @descripcion Configura un permiso de una cuenta asociada dependiendo las caracteristicas del mismo en su creacion y modificacion
	 * @author Valery Ibarra
	 * @fecha 24/11/2018
	 * @param permiso Permiso con la informacion a modificar o crear en la base de datos
	 * @returns Permiso configurado
	 * @throws Exception si los usuarios ingresados son nulos o no cumplen lo
	 *                   estipulado, asimismo con el nombre de cuenta o sucede algun
	 *                   error en la consulta a la base de datos
	 */
	public Permiso configurarPermisoAsociado(Permiso permiso) throws Exception {
		return iInformacionUsuarioRemota.configurarPermisoAsociado(permiso);
	}
	
	/**
	 * @descripcion Crea un nuevo usuario y le da permisos al usuario actual de lectura y escritura 
	 * @author Valery Ibarra
	 * @fecha 19/11/2018
	 * @param permiso Permiso que tienen los usuarios creadores y el que se creara ademas de lo necesario para el permiso
	 * @returns El usuario que fue creado
	 * @throws Exception Si no encuentra los usuarios ingresados ni los permisos
	 */
	public Usuario crearUsuarioAsociado(Permiso permiso) throws Exception {
		Usuario usuarioDestino = crearUsuario(permiso.getUsuarioDestino());
		permiso.setUsuarioDestino(usuarioDestino);
		configurarPermisoAsociado(permiso);
		return usuarioDestino;
	}
	
	/**
	 * @descripcion Devuelve los permisos que concedio el usuario que entra como
	 *              parametro
	 * @author Valery Ibarra
	 * @fecha 21/11/2018
	 * @param usuarioDestino Usuario que ha concedido permisos
	 * @returns Lista de permisos que ha concedido el usuario que se ingresa
	 */
	public List<Permiso> darPermisosOrigenDe(Usuario usuarioDestino) throws Exception{
		return iInformacionUsuarioRemota.darPermisosOrigenDe(usuarioDestino);
	}
	
	
	/**
	 * @descripcion Sugiere centros de vacunación dependiendo de la ciudad pasada por parámetro
	 * @author Ana Arango
	 * @fecha 21/11/2018
	 * @param ciudad ciudad en la que los centros de vacunación deben estar
	 * @throws Exception
	 */

	public List<CentroVacunacion> sugerirCentrosVacunacionCercanos(Ciudad ciudad) {
		return iCentrosVacunacionRemota.sugerirCentrosVacunacionCercanos(ciudad);
	}
	
	/**@author Ana Arango
	 * @fecha 24/11/2018
	 * @description Encontrar todos los esquemas que no pertenezcan al país entrado por parámetro
	 * @param idPais código del país que no se quiere mostrar sus esquemas
	 * @throws Exception Puede ocurrir Exception cuando el país del usuario en sesión es null
	 */
	public List<EsquemaDTO> encontrarEsquemasPorPais (Long idPais) 
			throws Exception{
		return iControlEsquemaVacunacionRemota.encontrarEsquemasPorPais(idPais);
	}
	
	/**@author Ana Arango
	 * @fecha 24/11/2018
	 * @description Encontrar todos los paises exceptuando el país por parámetro
	 * @param idPais código del país que no se quiere mostrar
	 */
	public List<Pais> encontrarPaisesExcepto (Long idPais) 
			throws Exception{
		return iControlEsquemaVacunacionRemota.encontrarPaisExcepto(idPais);
	}
	
	/**
	 * @descripcion Crea un usuario y una cuenta
	 * @author Diego Lamus
	 * @fecha 28/11/2018
	 * @param usuario Usuario que quiere crearse
	 * @param cuenta Cuenta del usuario que se quiere crear
	 * @returns Usuario creado con la cuenta asignada
	 * @throws Exception si no es posible crear la cuenta o le usuario
	 */
	public Usuario crearCuentaUsuario(Usuario usuario, Cuenta cuenta) throws Exception {
		return iInformacionUsuarioRemota.crearCuentaUsuario(usuario, cuenta);
	}
	
	/**
	 * @descripcion Busca por medio del correo un usuario ya registrado y le otorga permisos
	 * @author Valery Ibarra
	 * @fecha 28/11/2018
	 * @param permiso Tiene toda la informacion del permiso otorgado
	 * @param correo Es el correo del usuario al que se le otorgaran permisos, es decir, el que recibira
	 * @returns Lista de permisos que ha concedido el usuario que se ingresa
	 */
	public Permiso concederPermisoCorreo(Permiso permiso, String correo) throws Exception {
		return iInformacionUsuarioRemota.concederPermisoCorreo(permiso, correo);
	}
	
	/**
	 * @descripcion Devuelve los permisos que tengo sobre otros usuarios aun los ignorados
	 * @author Valery Ibarra
	 * @fecha 21/11/2018
	 * @param usuarioOrigen Usuario que tiene permisos y se desea saber sobre cuales
	 *                      usuarios tiene dicho permiso
	 * @returns Lista de permisos que tiene el usuario
	 * @throws Exception Si no encuentra el usuario ingresado en los permisos
	 */
	public List<Permiso> darPermisosDestinoDe(Usuario usuarioOrigen) throws Exception {
	 return iInformacionUsuarioRemota.darPermisosDestinoDe(usuarioOrigen);
	}
	
	/**
	 * @descripcion Devuelve los permisos que tengo es decir los permisos recibidos que estan en espera
	 * @author Valery Ibarra
	 * @fecha 04/12/2018
	 * @param usuarioOrigen Usuario que tiene permisos y se desea saber sobre cuales
	 *                      usuarios tiene dicho permiso en espera
	 * @returns Lista de permisos en los que el usuario es origen
	 */
	public List<Permiso> darPermisosDestinoDeEnEspera(Usuario usuarioOrigen) throws Exception {
		return iInformacionUsuarioRemota.darPermisosDestinoDeEnEspera(usuarioOrigen);
	}
	
	/**
	 * @descripcion Realiza la auditoria del registro de un usuario
	 * @author Victor Potes
	 * @fecha 30/11/2018
	 * @param usuarioRegistrado Usuario al que se le va a auditar su registro
	 * @throws Exception si no es posible auditar el registro del usuario
	 */
	public void auditarRegistro(Usuario usuarioRegistrado) throws Exception {
		iInformacionUsuarioRemota.auditarRegistro(usuarioRegistrado);
	}
	
	/**
	 * @descripcion Realiza la auditoria de agregar un esquema de vacunacion al carne
	 * @author Victor Potes
	 * @fecha 30/11/2018
	 * @param esquemaAgregado Esquema que se agregó al carne de vacinacion
	 * @param usuarioEnSesion usuario que se encarga de realizar la accion de agregar el esquema al carne
	 * @throws Exception si no es posible auditar agregar un esquema al carne de vacunacion
	 */
	public void auditarAgregarEsquemaAlCarne(EsquemaAgregado esquemaAgregado, Usuario usuarioEnSesion) throws Exception {
		iControlEsquemaVacunacionRemota.auditarAgregarEsquemaAlCarne(esquemaAgregado, usuarioEnSesion);
	}
	
	/**
	 * @descripcion Realiza la auditoria de eliminar una vacuna de un esquema en el carne
	 * @author Victor Potes
	 * @fecha 30/11/2018
	 * @param vacunaOculta Vacuna que se elimino del esquema en el carne
	 * @param usuarioEnSesion usuario que se encarga de realizar la accion de eliminar la vacuna
	 * @throws Exception si no es posible auditar eliminar una vacuna de un esquema en el carne
	 */
	public void auditarEliminarVacunaDeEsquemaEnCarne(VacunaOculta vacunaOculta, Usuario usuarioEnSesion) throws Exception {
		iControlCarneVacunacionRemota.auditarEliminarVacunaDeEsquemaEnCarne(vacunaOculta, usuarioEnSesion);
	}
	
	/**
	 * @descripcion Realiza la auditoria de la autenticación de un usuario
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuentaAnteriorLogin Cuenta que contiene la fecha de inicio de sesión anterior
	 * @param cuentaLogin Cuenta que contiene la fecha de inicio de sesión actual
	 * @throws Exception si no es posible auditar la autenticación del usuario
	 */
	public void auditarAutenticacion(Cuenta cuentaAnteriorLogin, Cuenta cuentaInicioSesion) throws Exception{
		iInformacionUsuarioRemota.auditarAutenticacion(cuentaAnteriorLogin, cuentaInicioSesion);
	}
	
	/**
	 * @descripcion Realiza la auditoria de la cerrar la sesión del usuario
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuentaAnteriorCerrarSesion Cuenta que contiene la fecha de cerrar sesión anterior
	 * @param cuentaCerrarSesion Cuenta que contiene la fecha de cerrar sesión actual
	 * @throws Exception si no es posible auditar el cerrado de sesión del usuario
	 */
	public void auditarCerrarSesion(Cuenta cuentaAnteriorCerrarSesion, Cuenta cuentaCerrarSesion) throws Exception{
		iInformacionUsuarioRemota.auditarCerrarSesion(cuentaAnteriorCerrarSesion, cuentaCerrarSesion);
	}
	
	/**
	 * @descripcion Se audita la creación de un usuario asociado
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param usuarioRegistrado Es el usuario asociado que se crea
	 * @param usuarioCreador Es el usuario quien realiza la acción de crear el usuario asociado
	 * @throws Exception en caso de existir lguna inconsistencia en los datos
	 */
	public void auditarCrearUsuarioAsociado(Usuario usuarioCreador, Usuario usuarioRegistrado) throws Exception {
		iInformacionUsuarioRemota.auditarCrearUsuarioAsociado(usuarioCreador, usuarioRegistrado);;
		
	}
	
	/**
	 * @descripcion Realiza la auditoria de modificar la aplicacion de una dosis en una vacuna
	 * @author Victor Potes
	 * @fecha 06/12/2018
	 * @param dosisAplicadaAnterior dosis aplicada que contiene los datos anteriores a la dosis aplicada actual
	 * @param dosisAplicadaActual dosis aplicada actual
	 * @param usuarioEnSesion usuario que realiza la acción de modificar la aplicación de la dosis
	 * @throws Exception si no es posible auditar la modificacion de una dosis aplicada
	 */
	public void auditarModificarAplicacionDosis(DosisAplicada dosisAplicadaAnterior, DosisAplicada dosisAplicadaActual,
			Usuario usuarioEnSesion) throws Exception {
		iControlCarneVacunacionRemota.auditarModificarAplicacionDosis(dosisAplicadaAnterior, dosisAplicadaActual, usuarioEnSesion);
	}

	/**
	 * @descripcion Se audita la modificación de perfil de un usuario
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param usuarioAnterior Es el usuario antes de efectuarle los cambios de la modificación
	 * @param usuarioModificado Es el usuario después de efectuarle los cambios de la modificación
	 * @param usuarioCreador Es el usuario quien realiza la acción de modificar el perfil
	 * @param cambioContrasenia true si el usuario cambió la contraseña, false en caso contrario
	 * @throws Exception en caso de existir lguna inconsistencia en los datos
	 */
	public void auditarModificarPerfil (Usuario usuarioAnterior, Usuario usuarioModificado, Usuario usuarioCreador, boolean cambioContrasenia) throws Exception {
		iInformacionUsuarioRemota.auditarModificarPerfil(usuarioAnterior, usuarioModificado, usuarioCreador, cambioContrasenia);
	}
	
	/**
	 * @descripcion actualiza la fecha de la cuenta, se usa cuando se vaya a cerrar sesión
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuenta cuenta a la que se le actualizará la fecha de cerrado de sesión
	 * @throws Exception si no es posible actualizar la fecha de cerrar sesión de la cuenta
	 */
	public Cuenta actualizarFechaCerrarSesion(Cuenta cuenta) throws Exception{
		return iInformacionUsuarioRemota.actualizarFechaCerrarSesion(cuenta);
	}
	
}
