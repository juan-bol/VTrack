package co.com.tracert.vtrack.logic.ejb;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.com.tracert.vtrack.logic.manager.LoginManager;
import co.com.tracert.vtrack.logic.manager.PermisosManager;
import co.com.tracert.vtrack.logic.manager.UsuarioManager;
import co.com.tracert.vtrack.model.entities.Cuenta;
import co.com.tracert.vtrack.model.entities.Menu;
import co.com.tracert.vtrack.model.entities.Notificacion;
import co.com.tracert.vtrack.model.entities.Permiso;
import co.com.tracert.vtrack.model.entities.Usuario;
import co.com.tracert.vtrack.model.interfaces.IInformacionUsuarioLocal;
import co.com.tracert.vtrack.model.interfaces.IInformacionUsuarioRemota;

/*he comentado esta  linea para establecer stateful y que me garantize el estado de la sesión*/
@Stateless(name="InformacionUsuarioBean", mappedName="informacionUsuarioBean")
//@Stateful(name="InformacionUsuarioBean", mappedName="informacionUsuarioBean")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Local(IInformacionUsuarioLocal.class)
@Remote(IInformacionUsuarioRemota.class)
public class InformacionUsuarioBean implements IInformacionUsuarioLocal, IInformacionUsuarioRemota{

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * @author Diego Lamus
	 * @Descripcion busca un usuario en la base de datos
	 * @param id id del usuario que se quiere buscar en la base de datos
	 * @return usuario - instancia del usuario que se encontro en la base de datos
	 * @throws Exception si se genera algun error en la base de datos o el id del usuario no es valida
	 */
	@Override
	public Usuario consultarUsuario(long id) throws Exception {
		UsuarioManager manager = new UsuarioManager(entityManager);
		return manager.consultarUsuario(id);
	}

	
	/**
	 * @author Santiago
	 * @param objeto usuario que se quiere consultar en la base de datos
	 * @return Listado de menus correspondientes al usuario
	 * @throws Exception si se genera algun error en la base de datos o el id del usuario no es valida
	 */
	@Override
	public List<Menu> consultarMenuUsuario(Usuario usuario) throws Exception {
		LoginManager loginManager= new LoginManager(entityManager);
		return loginManager.consultarMenuUsuario(usuario);
	}
	
	
	/**
	 * @author Diego lamus
	 * @descripción se deberá traer el usuario a traves de la cuenta basado en su correo y contraseña recibidos por parametro.
	 * @returns Cuenta cuenta del usuario que hace inicio de sesion
	 * @throws Exception En caso que no se encuentre se al usuario o su contraseña no sea valida
	 * @fecha 27/10/2018
	 */
	@Override
	public Usuario iniciarSesion(Cuenta cuentaUsuario) throws Exception {
		LoginManager manager = new LoginManager(entityManager);
		return manager.autenticarUsuario(cuentaUsuario);
	}

		/**
	 * @author Juan Bolaños - Valery Ibarra
	 * @description Se registra un usuario
	 * @param usuario que se desea crear
	 * @throws Exception
	 */
	@Override
	public Usuario crearUsuario(Usuario usuario) throws Exception {
		UsuarioManager manager = new UsuarioManager(entityManager);
		return manager.crearUsuario(usuario);
		
	}


	/**
 * @author Victor Potes
 * @description Se crea una cuenta
 * @param cuenta que se va a crear
 * @throws Exception Excepcion por reglas del negocio
 */
	@Override
	public Cuenta crearCuenta(Cuenta cuenta) throws Exception {
		UsuarioManager usuManager = new UsuarioManager(entityManager);
		return usuManager.crearCuenta(cuenta);
		
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
	@Override
	public List<Permiso> darPermisosDestinoDeAprobado(Usuario usuarioOrigen) throws Exception {
		PermisosManager perManager = new PermisosManager(entityManager);
		 return perManager.darPermisosDestinoDeAprobado(usuarioOrigen);
	}


	 /** 
     * @descripcion modifica la cuenta ingresada por parametro
	 * @author Victor Potes
	 * @fecha 3/11/2018
	 * @param cuenta que se va a modificar
	 * @returns la cuenta modificada
	 * @throws Exception Excepciones de reglas de negocio
	 */
	@Override
	public Cuenta modificarCuenta(Cuenta cuenta) throws Exception {
		UsuarioManager usuManager = new UsuarioManager(entityManager);
		return usuManager.modificarCuenta(cuenta);
	}

	/**
	 * @descripcion Modifica el usuario ingresado
	 * @author Valery Ibarra
	 * @fecha 3/11/2018
	 * @param usuario Usuario que se va a modificar
	 * @returns El usuario modificado
	 * @throws Exception Excepciones de reglas de negocio
	 */
	@Override
	public Usuario modificarUsuario(Usuario usuario) throws Exception {
		UsuarioManager usuManager = new UsuarioManager(entityManager);
		return usuManager.modificarUsuario(usuario);
	}

	/** 
     * @descripcion Vincula usuarios, es decir, cambia en usuario su cuenta y cambia en cuenta su usuario
	 * @author Valery Ibarra
	 * @fecha 6/11/2018
	 * @param cuenta que se va a vincular, es decir, que se le va asignar su usuario
	 * @param usuario que se va a vincular, es decir, que se le va asignar su cuenta
	 * @returns usuario que ya tiene la cuenta vinculada
	 * @throws Exception Excepciones de reglas de negocio
	 */
	@Override
	public Usuario vincularCuentaUsuario(Cuenta cuenta, Usuario usuario) throws Exception {
		UsuarioManager usuManager = new UsuarioManager(entityManager);
		return usuManager.vincularCuentaUsuario(cuenta, usuario);
	}


	/**
	 * @author Victor Potes
	 * @descripción Busca la cuenta de acuerdo a su correo
	 * @fecha 10/11/2018
	 * @parametro correo correo de la cuenta que se piensa retornar
	 * @returns Cuenta cuenta que tiene el correo ingresado por parametro
	 * @throws Exception En caso que no se encuentre encuentre la cuenta, significa
	 *                   que aun no se ha registrado con ese correo
	 */
	@Override
	public Cuenta encontrarCuentaPorCorreo(String correo) throws Exception {
		//TODO Agregar encontrarCuentaPorCorreo al deployment
		UsuarioManager usuManager = new UsuarioManager(entityManager);
		return usuManager.encontrarCuentaPorCorreo(correo);
	}


	@Override
	public void modificarNotificacion(Notificacion notificacion) throws Exception{
		UsuarioManager usuarioManager = new UsuarioManager(entityManager);
		usuarioManager.modificarNotificacion(notificacion);
	}

	@Override
	public Notificacion crearNotificacion(Notificacion notificacion) throws Exception{
		UsuarioManager usuarioManager = new UsuarioManager(entityManager);
		return usuarioManager.crearNotificacion(notificacion);
	}

	/**
	 * @descripcion Configura un permiso dependiendo las caracteristicas del mismo en su creacion y modificacion
	 * @author Valery Ibarra
	 * @fecha 19/11/2018
	 * @param permiso Permiso con la informacion a modificar o crear en la base de datos
	 * @throws Exception si los usuarios ingresados son nulos o no cumplen lo
	 *                   estipulado, asimismo con el nombre de cuenta o sucede algun
	 *                   error en la consulta a la base de datos
	 */
	@Override
	public Permiso configurarPermisoGeneral(Permiso permiso) throws Exception {
		PermisosManager perManager = new PermisosManager(entityManager);
		return perManager.configurarPermisoGeneral(permiso);
	}
	
	/**
	 * @descripcion Configura un permiso de una cuenta asociada dependiendo las caracteristicas del mismo en su creacion y modificacion
	 * @author Valery Ibarra
	 * @fecha 24/11/2018
	 * @param permiso Permiso con la informacion a modificar o crear en la base de datos
	 * @throws Exception si los usuarios ingresados son nulos o no cumplen lo
	 *                   estipulado, asimismo con el nombre de cuenta o sucede algun
	 *                   error en la consulta a la base de datos
	 */
	@Override
	public Permiso configurarPermisoAsociado(Permiso permiso) throws Exception {
		PermisosManager perManager = new PermisosManager(entityManager);
		return perManager.configurarPermisoAsociado(permiso);
	}
	
	/**
	 * @descripcion Devuelve los permisos que concedio el usuario que entra como parametro
	 * @author Valery Ibarra
	 * @fecha 21/11/2018
	 * @param usuarioDestino Usuario que ha concedido permisos
	 * @returns Lista de permisos que ha concedido el usuario que se ingresa
	 */
	@Override
	public List<Permiso> darPermisosOrigenDe(Usuario usuarioDestino) throws Exception {
		PermisosManager perManager = new PermisosManager(entityManager);
		return perManager.darPermisosOrigenDe(usuarioDestino);
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
	@Override
	public List<Permiso> darPermisosDestinoDe(Usuario usuarioOrigen) throws Exception {
		PermisosManager perManager = new PermisosManager(entityManager);
		 return perManager.darPermisosDestinoDe(usuarioOrigen);
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
	@Override
	public Usuario crearCuentaUsuario(Usuario usuario, Cuenta cuenta) throws Exception {
		UsuarioManager manager = new UsuarioManager(entityManager);
		return manager.crearCuentaUsuario(usuario, cuenta);
	}

	/**
	 * @descripcion Busca por medio del correo un usuario ya registrado y le otorga permisos
	 * @author Valery Ibarra
	 * @fecha 28/11/2018
	 * @param permiso Tiene toda la informacion del permiso otorgado
	 * @param correo Es el correo del usuario al que se le otorgaran permisos, es decir, el que recibira
	 * @returns Lista de permisos que ha concedido el usuario que se ingresa
	 */
	@Override
	public Permiso concederPermisoCorreo(Permiso permiso, String correo) throws Exception {
		PermisosManager perManager = new PermisosManager(entityManager);
		 return perManager.concederPermisoCorreo(permiso, correo);
	}


	/**
	 * @descripcion Realiza la auditoria del registro de un usuario
	 * @author Victor Potes
	 * @fecha 30/11/2018
	 * @param usuarioRegistrado Usuario al que se le va a auditar su registro
	 * @throws Exception si no es posible auditar el registro del usuario
	 */
	@Override
	public void auditarRegistro(Usuario usuarioRegistrado) throws Exception {
		UsuarioManager manager = new UsuarioManager(entityManager);
		manager.auditarRegistro(usuarioRegistrado);	
	}
	
	/**
	 * @descripcion Realiza la auditoria de la autenticación de un usuario
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuentaAnteriorLogin Cuenta que contiene la fecha de inicio de sesión anterior
	 * @param cuentaLogin Cuenta que contiene la fecha de inicio de sesión actual
	 * @throws Exception si no es posible auditar la autenticación del usuario
	 */
	@Override
	public void auditarAutenticacion(Cuenta cuentaAnteriorLogin, Cuenta cuentaInicioSesion) throws Exception{
		LoginManager manager = new LoginManager(entityManager);
		manager.auditarAutenticacion(cuentaAnteriorLogin, cuentaInicioSesion);	
	}
	
	/**
	 * @descripcion Realiza la auditoria de la cerrar la sesión del usuario
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuentaAnteriorCerrarSesion Cuenta que contiene la fecha de cerrar sesión anterior
	 * @param cuentaCerrarSesion Cuenta que contiene la fecha de cerrar sesión actual
	 * @throws Exception si no es posible auditar el cerrado de sesión del usuario
	 */
	@Override
	public void auditarCerrarSesion(Cuenta cuentaAnteriorCerrarSesion, Cuenta cuentaCerrarSesion) throws Exception {
		LoginManager manager = new LoginManager(entityManager);
		manager.auditarCerrarSesion(cuentaAnteriorCerrarSesion, cuentaCerrarSesion);	
		
	}
	

	/**
	 * @descripcion actualiza la fecha de la cuenta, se usa cuando se vaya a cerrar sesión
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param cuenta cuenta a la que se le actualizará la fecha de cerrado de sesión
	 * @throws Exception si no es posible actualizar la fecha de cerrar sesión de la cuenta
	 */
	@Override
	public Cuenta actualizarFechaCerrarSesion(Cuenta cuenta) throws Exception {
		LoginManager manager = new LoginManager(entityManager);
		return manager.actualizarFechaCerrarSesion(cuenta);
	}


	/**
	 * @descripcion Se audita la creación de un usuario asociado
	 * @author Victor Potes
	 * @fecha 09/12/2018
	 * @param usuarioRegistrado Es el usuario asociado que se crea
	 * @param usuarioCreador Es el usuario quien realiza la acción de crear el usuario asociado
	 * @throws Exception en caso de existir lguna inconsistencia en los datos
	 */
	@Override
	public void auditarCrearUsuarioAsociado(Usuario usuarioCreador, Usuario usuarioRegistrado) throws Exception {
		UsuarioManager manager = new UsuarioManager(entityManager);
		manager.auditarCrearUsuarioAsociado(usuarioCreador, usuarioRegistrado);	
		
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
	@Override
	public void auditarModificarPerfil (Usuario usuarioAnterior, Usuario usuarioModificado, Usuario usuarioCreador, boolean cambioContrasenia) throws Exception{
		UsuarioManager manager = new UsuarioManager(entityManager);
		manager.auditarModificarPerfil(usuarioAnterior, usuarioModificado, usuarioCreador, cambioContrasenia);
	}

	
	/**
	 * @descripcion Devuelve los permisos que tengo es decir los permisos recibidos que estan en espera
	 * @author Valery Ibarra
	 * @fecha 04/12/2018
	 * @param usuarioOrigen Usuario que tiene permisos y se desea saber sobre cuales
	 *                      usuarios tiene dicho permiso en espera
	 * @returns Lista de permisos en los que el usuario es origen
	 */
	@Override
	public List<Permiso> darPermisosDestinoDeEnEspera(Usuario usuarioOrigen) throws Exception {
		PermisosManager perManager = new PermisosManager(entityManager);
		 return perManager.darPermisosDestinoDeEnEspera(usuarioOrigen);
	}
	
	
}
